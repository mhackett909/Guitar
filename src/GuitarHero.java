/**
 * Guitar Hero Project
 * Simply press a key from (String) keyboard to play a note!
 * Watch as your sound wave is converted into visual form!
 * 
 * @author Michael Hackett
 * @version 1.0
 * @since 3/3/2020
 *
 * Class: Data Structures and Algorithms (CSCI 230-55)
 * Professor: Frank Madrid
 * Credit: StdDraw and StdAudio from introcs.cs.princeton.edu
 */
public class GuitarHero {
	public static void main(String[] guitar) {
		//Question: throws RingBuffer.RingBufferException eliminates try/catch ???
		final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' "; 
		GuitarString[] strings = new GuitarString[keyboard.length()];
		int counter = 0, index;
		char key;
		double[] coords = reset();
		double sample;
		try {
			/**
			 * To represent 37 guitar strings, an array of GuitarString objects is used. Each new GuitarString has its own
			 * frequency ranging from 110 Hz to 880 Hz, ascending, which is calculated using a formula. When a key from 
			 * (String) keyboard is pressed, the corresponding GuitarString is plucked.
			 */
			for (int x = 0; x < keyboard.length(); x++) strings[x] = new GuitarString(440 * Math.pow(1.05956,x-24));
			while (true) {
				if (StdDraw.hasNextKeyTyped()) {
					key = StdDraw.nextKeyTyped();
					index = keyboard.indexOf(key);
					if (index > -1) strings[index].pluck();
				}
				/**
				 * I implemented the isPlucked() method to avoid unnecessarily calling sample() and tic() on every GuitarString.
				 * For added efficiency, (boolean) plucked resets once the samples are sufficiently close to zero. 
				 * However, if a user must replace a GuitarString with a new "pre-plucked" GuitarString(double[]), 
				 * the algorithm will not call sample() and tic() on it. The isPlucked() call can be removed if necessary.
				 */
				sample = 0;
				for (int x = 0; x < keyboard.length(); x++) 
					if (strings[x].isPlucked()) {
						sample += strings[x].sample();
						strings[x].tic();
					}

				StdAudio.play(sample);
				/**
				 * To avoid choppy sound, I use strings[0].time() % 600 to delay the draw functions.
				 * Since strings[0].tic() is called regardless of whether strings[0] is plucked, calling strings[0].time()
				 * returns an inaccurate result when !strings[0].isPlucked(). For more accurate time() calls, (int) pluck
				 * is reset when pluck() is called.
				 * 
				 * The visual sound waves are designed to appear like a heart monitor. A filled white rectangle is constantly clearing
				 * out the area just ahead of the new lines being drawn. 100 intervals are used for drawing. Coordinates are based
				 * on x intervals of 1/100 (0.01) and the y value of 0.5 + the sample.
				 */

				if (!strings[0].isPlucked()) strings[0].tic(); 
				if (strings[0].time() % 500 == 0) {
					if (counter == 100) { 
						counter = 0;
						coords = reset();
					}
					StdDraw.setPenColor(StdDraw.WHITE);
					StdDraw.filledRectangle(coords[0]+0.015, 0.5, 0.015, 1);
					StdDraw.setPenColor(StdDraw.BLUE);
					StdDraw.line(coords[0],coords[1],coords[2],0.5+sample);
					coords[0] += 0.01;
					coords[1] = 0.5 + sample;
					coords[2] += 0.01;
					counter++;
				}
			}
		}catch (RingBuffer.RingBufferException x) { 
			System.out.println(x.getMessage()); 
			System.exit(1);
		}
	}
	private static double[] reset() { //Default drawing coordinates
		double[] w = {0, 0.5, 0.01};
		return w;
	}
}