public class MemoryAllocation {
    public static void main(String[] args) {
        long initialMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Initial memory: " + initialMemory);

        byte[] memoryBlock = new byte[1024 * 1024 * 100]; // Allocate 100 MB of memory

        long finalMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Final memory: " + finalMemory);

        System.out.println("Memory allocated: " + (finalMemory - initialMemory));

        memoryBlock = null; // Set the memory block to null to allow garbage collection

        System.gc(); // Request garbage collection

        long postGcMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.println("Memory after garbage collection: " + postGcMemory);
    }
}
This Java program calculates the initial memory usage, allocates 100 MB of memory, and then calculates the final memory usage. It then sets the memory block to null and requests garbage collection. Finally, it prints the memory usage after garbage collection.
Again, please remember that using this tool in production environments or real-world applications is discouraged due to the potential negative performance impact and hardware wear.
