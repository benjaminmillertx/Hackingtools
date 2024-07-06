To create a full-blown tool to block drivers from loading using a name collision technique in C, you will need to have a good understanding of the Windows Driver Model (WDM) and the driver subsystem in the Windows operating system. You will also need to have experience programming in C and be familiar with low-level programming concepts such as memory management and pointer arithmetic.

Here is an outline of the steps you can follow to create a tool to block drivers from loading using a name collision technique in C:

1. **Create a dummy driver**: The first step is to create a dummy driver with the same name as the target driver. This can be done by creating a new file with the same name as the target driver in the `%SystemRoot%\System32\drivers` directory.
2. **Modify the `DRIVER_ENTRY_DATA_BLOCK` structure**: In order to prevent the target driver from loading, you will need to modify the `DRIVER_ENTRY_DATA_BLOCK` structure for the target driver and replace its `DriverEntry` function with a dummy function. This can be done by using a hex editor to modify the binary file for the target driver, or by using a hooking library to intercept the `DriverEntry` function call.
3. **Implement the dummy driver**: Once you have modified the `DRIVER_ENTRY_DATA_BLOCK` structure, you will need to implement the dummy driver. This can be a simple function that returns an error code or fails to load.
4. **Test the tool**: After you have implemented the tool, you will need to test it to ensure that it is blocking the target driver from loading correctly. You can do this by using the `devcon.exe` command-line tool or the Device Manager to check the status of the target driver.

Here is a basic example of how you can modify the `DRIVER_ENTRY_DATA_BLOCK` structure for a target driver in C:

```
#include <windows.h>

// Function prototype for the dummy function
NTSTATUS dummy_driver_entry(DRIVER_OBJECT *driver_object, UNICODE_STRING *registry_path);

// Address of the target driver's `DRIVER_ENTRY_DATA_BLOCK` structure
PDRIVER_ENTRY_DATA_BLOCK target_driver_entry;

// Address of the target driver's `DriverEntry` function
PDRIVER_ENTRY target_driver_entry_original;

// Intercept the `DriverEntry` function call and replace it with the dummy function
NTSTATUS fake_driver_entry(DRIVER_OBJECT *driver_object, UNICODE_STRING *registry_path) {
  // Call the dummy function
  return dummy_driver_entry(driver_object, registry_path);
}

// The dummy function that will be called instead of the target driver's `DriverEntry` function
NTSTATUS dummy_driver_entry(DRIVER_OBJECT *driver_object, UNICODE_STRING *registry_path) {
  // Return an error code
  return STATUS_UNSUCCESSFUL;
}

int main() {
  // Get the address of the target driver's `DRIVER_ENTRY_DATA_BLOCK` structure
  target_driver_entry = (PDRIVER_ENTRY_DATA_BLOCK)0x7FFE0000; // replace with the actual address

  // Get the address of the target driver's `DriverEntry` function
  target_driver_entry_original = (PDRIVER_ENTRY)target_driver_entry->DriverInit;

  // Replace the target driver's `DriverEntry` function with the fake function
  target_driver_entry->DriverInit = fake_driver_entry;

  // Return success
  return 0;
}
```

Keep in mind that using name collision techniques to block drivers can be considered a malicious activity and may violate the terms of service for the operating system and hardware. It is generally recommended to use official methods for managing drivers, such as using the Device Manager or the `devcon.exe` command-line tool.
