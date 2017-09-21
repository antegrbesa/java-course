package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Models a Newton-Raphson iteration-based fractal viewer.
 *	Arguments are entered into console. Keyword for terminating is {@code done}. 
 * 
 * @author Ante Grbesa
 *
 */
public class Newton {

	/**
	 * Main method
	 * @param args not used
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");
		
		List<Complex> list = new ArrayList<>();
		int i = 1;
		System.out.print("Root "+i+"> ");
		while (sc.hasNext()) {
			
			String line = sc.nextLine();
			if (line.equals("done")) {
				if (i > 2) {
					System.out.println("Image of fractal will appear shortly..");
					Complex[] arr = new Complex[list.size()];
					list.toArray(arr);
					FractalViewer.show(new MyProducer(new ComplexRootedPolynomial(arr)));
					return;
				} else {
					System.out.println("Not enough roots, cannot generate fractal.");
					System.exit(1);
				}
				
			}
			
			Complex c = null;
			try {
				c = parse(line);
			} catch (IllegalArgumentException e) {
				System.out.println("Format invalid.");
				System.out.print("Root "+i+"> ");
				continue;
			}
			
			list.add(c);
			++i;
			System.out.print("Root "+i+"> ");
		}
		
	}
	
	/**
	 * Parses given string to a {@link Complex} number.
	 * @param s string to parse 
	 * @return parsed complex number
	 */
	private static Complex parse(String s) {
		if (s == null) {
			throw new IllegalArgumentException();
		}

		double re;
		double im;
		
		// Only real part
		if(!s.contains("i")){
			re = Double.parseDouble(s);
			im = 0;
		} else if(!s.matches(".*\\d\\D\\d.*") && !s.matches(".*\\d\\D\\D.*")){	// Only imaginary part
			re = 0;
			if(!s.matches(".*\\di.*")){
				s=s.replace("i", "1");
			}else{
				s = s.replace("i", "");
			}
			im = Double.parseDouble(s);
		} else { // Real and imaginary part
			int sign1=1, sign2=1;
			String[] numbers;
			if(s.startsWith("+")){
				s=s.substring(1);
			}
			
			if (s.startsWith("-")){	
				sign1 = -1;			
				s=s.substring(1); 
				numbers = s.split("\\+"); 
				if(numbers.length != 2){
					numbers = s.split("\\-");
					sign2 = -1;
				}
			} else {					
				numbers = s.split("\\+"); 
				if(numbers.length != 2){
					numbers = s.split("\\-");
					sign2 = -1;
				}
			}
			
			//imaginary part is only "i"
			if (numbers[1].startsWith("i") || numbers[1].contains("-i")){
				numbers[1]=numbers[1].replace("i", "1");
			} else {
	
				numbers[1] = numbers[1].replace("i", ""); 
			}
			re = Double.parseDouble(numbers[0]) * sign1;
			im = Double.parseDouble(numbers[1]) * sign2;
					
		}
		return new Complex(re,im);
	}
	
	/**
	 * Implementation of a job for a thread. 
	 * @author Ante
	 *
	 */
	public static class CalculationJob implements Callable<Void> {
		double reMin;
		double reMax;
		double imMin;
		double imMax;
		int width;
		int height;
		int yMin;
		int yMax;
		int m;
		short[] data;
		ComplexRootedPolynomial roots;
		
		private static final double CONVERGENCE_THRESHOLD = 1E-3;
		private static final double ROOT_THRESHOLD = 2E-3;
		
		/**
		 * Constructs this class with specified argument.
		 * @param reMin
		 * @param reMax
		 * @param imMin
		 * @param imMax
		 * @param width
		 * @param height
		 * @param yMin
		 * @param yMax
		 * @param m
		 * @param data
		 * @param roots
		 */
		public CalculationJob(double reMin, double reMax, double imMin,
				double imMax, int width, int height, int yMin, int yMax, 
				int m, short[] data, ComplexRootedPolynomial roots) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.roots = roots;
		}
		
		@Override
		public Void call() {
			
			calculate();
			
			return null;
		}
		
		/**
		 * Calculates specified area in an array for fractal generation. 
		 */
		private void calculate() {
			int offset = yMin * width;
			
			ComplexPolynomial polynomial = roots.toComplexPolynom();
			ComplexPolynomial derived = polynomial.derive();
			for(int y = yMin; y <= yMax; y++) {
				for(int x = 0; x < width; x++) {
					double cre = x / (width-1.0) * (reMax - reMin) + reMin;
					double cim = (height-1.0-y) / (height-1) * (imMax - imMin) + imMin;
					Complex zn = new Complex(cre, cim);
										
					int iters = 0;
					double module = 0;
					Complex zn1 = null;
					do {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = derived.apply(zn);
						Complex fraction = numerator.divide(denominator);
						zn1 = zn.sub(fraction);
						module = (zn1.sub(zn)).module();
						zn = zn1;
						iters++;
					} while((iters < m) && (module > CONVERGENCE_THRESHOLD));
					
					short index = (short) roots.indexOfClosestRootFor(zn1, ROOT_THRESHOLD);
					
					if (index == -1) {
						data[offset++] = 0;
					} else {
						data[offset++] = index;
					}
				}
			}
		}
	}
	
	/**
	 * Implementation of a {@link IFractalProducer}.
	 * @author Ante
	 *
	 */
	public static class MyProducer implements IFractalProducer {
		/**Thread pool*/
		private ExecutorService pool;
		
		/**Roots*/
		private ComplexRootedPolynomial roots;
		
		/**
		 * Constructs this class with specified arguments
		 * @param roots roots to set
		 */
		public MyProducer(ComplexRootedPolynomial roots) {
			pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()
					, new DaemonicThreadFactory());
			this.roots = roots;
		}
		
		
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax,
				int width, int height, long requestNo, IFractalResultObserver observer) {
			System.out.println("Calculation started...");
			int m = 16*16;
			short[] data = new short[width * height];
			final int numberOfLines =8 * Runtime.getRuntime().availableProcessors();
			int numberOfYPerLine = height / numberOfLines;
			
			List<Future<Void>> results = new ArrayList<>();
			
			for (int i = 0; i < numberOfLines; i++) {
				int yMin = i*numberOfYPerLine;
				int yMax = (i+1)*numberOfYPerLine-1;
				if(i==numberOfLines-1) {
					yMax = height-1;
				}
				CalculationJob job = new CalculationJob(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, roots);
				results.add(pool.submit(job));
			}
			for (Future<Void> result : results) {
				try {
					result.get();
				} catch (InterruptedException | ExecutionException ignorable) {
				}
				
			}
	
			System.out.println("Calculation done. Notifying GUI..");
			observer.acceptResult(data, (short)(roots.toComplexPolynom().order()+1), requestNo);
		}
		
	}
	
	/**
	 * Implementation of a thread factory that creates daemonic threads. 
	 * @author Ante
	 *
	 */
	private static class DaemonicThreadFactory implements ThreadFactory {

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = Executors.defaultThreadFactory().newThread(r);
			thread.setDaemon(true);
			return thread;
		}
		
	}
	
	
	
}
