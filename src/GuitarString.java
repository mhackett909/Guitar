//Models a vibrating guitar string
public class GuitarString {
	private int tics = 0;
	private boolean plucked = false;
	private RingBuffer buffer;
	//Constructor for new GuitarString of a given frequency. All values set to zero.
	protected GuitarString(double frequency) throws RingBuffer.RingBufferException {
		buffer = new RingBuffer((int) Math.ceil(44100 / frequency)); //44100 = sampling rate
		for (int x = 0; x < buffer.max(); x++) buffer.enqueue(0.0);
	}
	//Constructor for a premade GuitarString. Copies size and values from a double[]
	protected GuitarString(double[] init) throws RingBuffer.RingBufferException {
		buffer = new RingBuffer(init.length);
		for (int x = 0; x < buffer.max(); x++) buffer.enqueue(init[x]);
	}
	//Clears the buffer and fills it with random values between -0.5 and 0.5
	protected void pluck() throws RingBuffer.RingBufferException {
		for (int x = 0; x < buffer.max(); x++) buffer.dequeue();
		for (int x = 0; x < buffer.max(); x++) buffer.enqueue(Math.random() - 0.5);
		plucked = true;
		tics = 0; //Reset tics when the string is plucked. Improves accuracy of time() calls.
	}
	//Karpus-Strong update to model decaying vibrations
	protected void tic() throws RingBuffer.RingBufferException {
		double x = buffer.peek();
		buffer.dequeue();
		buffer.enqueue((x + buffer.peek())/2 * 0.994); 
		tics++;
	}
	//Returns the first sample in the buffer.
	protected double sample() throws RingBuffer.RingBufferException {
		double x = buffer.peek();
		if (Math.abs(x) < 1E-10) plucked = false; //If sample() is sufficiently close to zero, reset plucked
		return x; 
	}
	protected int time() { return tics;	} //Number of times tic() is called after string is plucked.
	protected boolean isPlucked() { return plucked; } //Used for efficiency in GuitarHero while loop
}
