//Data structure which models a vibrating string tied down at both ends
//Implemented using a circular array
public class RingBuffer {
	private int max, size = 0, first = 0, last = 0;
	private double[] buffer;
	//Creates a new buffer of desired capacity
	protected RingBuffer(int capacity) { 
		max = capacity; 
		buffer = new double[max];
	}
	protected RingBuffer() { this(100); } //Default buffer with size 100
	protected int size() { return size; } //Returns the number of objects in the buffer
	protected int max() { return max; } //Returns the buffer's maximum capacity
	protected boolean isEmpty() { return (size == 0 ? true : false); } //Returns true if buffer is empty
	protected boolean isFull() { return (size == max ? true : false); } //Returns true if buffer is full
	//Adds a new double to the end of the queue
	protected void enqueue(double x) throws RingBufferException {
		if (isFull()) throw new RingBufferException("Buffer is full.");
		buffer[last] = x;
		last = (last + 1) % max; //Circular array equation from Goodrich - Data Structures and Algorithms
		size++;
	}
	//Removes and returns a double from the front of the queue
	protected double dequeue() throws RingBufferException {
		double x = peek();
		first = (first + 1) % max;
		size--;
		return x;
	}
	//Returns a double from the front of the queue
	protected double peek() throws RingBufferException {
		if (isEmpty()) throw new RingBufferException("Buffer is empty.");
		return buffer[first];
	}
	//Custom class for RingBuffer exceptions
	@SuppressWarnings("serial")
	protected class RingBufferException extends Exception {
		protected RingBufferException(String msg) { super(msg); }
	}
}