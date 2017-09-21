package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * A simple ray-caster implementation.  
 * @author Ante
 *
 */
public class RayCaster {

	/**
	 * This method starts once the program is started.
	 * @param args not used
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10,0,0),new Point3D(0,0,0), new Point3D(0,0,10), 20, 20);

	}

	/**
	 * Returns an implementation of a {@link IRayTracerProducer}.
	 * @return implementation of a {@link IRayTracerProducer}.
	 */
	private static IRayTracerProducer getIRayTracerProducer() { 
		return new IRayTracerProducer() {

			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp,
					double horizontal, double vertical, int width, int height, long requestNo, IRayTracerResultObserver observer) {
				System.out.println("Zapoèinjem izraèune..."); 
				short[] red = new short[width*height];
				short[] green = new short[width*height];
				short[] blue = new short[width*height];

				Point3D zAxis = view.sub(eye).modifyNormalize();
				Point3D vuv = viewUp.normalize();
				Point3D yAxis = vuv.sub(zAxis.scalarMultiply(zAxis.scalarProduct(vuv))).normalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();
				
				Point3D screenCorner = view.sub(xAxis.scalarMultiply(((double)horizontal)/2)).add(yAxis.scalarMultiply(((double)vertical)/2));

				Scene scene = RayTracerViewer.createPredefinedScene();

				short[] rgb = new short[3];
				int offset = 0;

				for(int y = 0; y < height; y++) {
					for(int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner.add
								(xAxis.scalarMultiply((((double)x)/(width-1))*horizontal)).sub(yAxis.scalarMultiply((((double)y)/(height-1))*vertical));

						Ray ray = Ray.fromPoints(eye, screenPoint);

						tracer(scene, ray, rgb, eye);

						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						offset++;

					}

				}

				System.out.println("Izraèuni gotovi..." );
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");

			}
			/**
			 * Calculates color for this pixel. 
			 * @param scene scene
			 * @param ray ray 
			 * @param rgb colors
			 * @param eye eye point
			 */
			private void tracer(Scene scene, Ray ray, short[] rgb, Point3D eye) {
				RayIntersection intersection = getIntersection(scene, ray);
				
				if (intersection == null) {
					rgb[0] = 0;
					rgb[1] = 0;
					rgb[2] = 0;
				} else {
					determineColor(scene, ray , rgb, intersection, eye);
				}

			}
			
			/**
			 * Returns closest intersection
			 * @param scene scene
			 * @param ray ray
			 * @return closest intersection
			 */
			private RayIntersection getIntersection(Scene scene, Ray ray) {
				RayIntersection intersection = null;
				double diff = 0.0;
				boolean first = true;
				RayIntersection intersected = null;
				for (GraphicalObject object : scene.getObjects()) {
					intersection = object.findClosestRayIntersection(ray);
					if (intersection == null) {
						continue;
					}
					
					if (first) {
						first = false;
						diff = intersection.getDistance();
						intersected = intersection;
						continue;
					}
					
					if (intersection.getDistance() < diff) {
						diff = intersection.getDistance();
						intersected = intersection;
					}
				}
				
				return intersected;
			}
			
			/**
			 * Determines color for pixel. 
			 * @param scene scene
			 * @param ray ray
			 * @param rgb rgb colors
			 * @param intersection intersection
			 * @param eye eye point
			 */
			private void determineColor(Scene scene, Ray ray, short[] rgb, RayIntersection intersection, Point3D eye) {
				rgb[0] = 15;
				rgb[1] = 15;
				rgb[2] = 15;
				for (LightSource ls : scene.getLights()) {
					Ray newRay =  Ray.fromPoints(ls.getPoint(), intersection.getPoint());
					RayIntersection newIntersection = getIntersection(scene, newRay);
					
					if (newIntersection != null) {
						if (ls.getPoint().sub(newIntersection.getPoint()).norm() + 0.01 < ls.getPoint().sub(intersection.getPoint()).norm()) {
							continue;
						}
					} 
					
					rgb[0] =  (short) (rgb[0] + getSumColor(ls, 0, intersection, eye));
					rgb[1] = (short) (rgb[1] + getSumColor(ls, 1, intersection, eye));
					rgb[2] = (short) (rgb[2] + getSumColor(ls, 2, intersection, eye));
				}
				
			}
			
			/**
			 * Returns a RGB value of calculated color based on Phongs' model.
			 * @param ls light source
			 * @param i identifier for rgb color
			 * @param intersection  intersection
			 * @param eye eye point
			 * @return RGB value of calculated color
			 */
			private short getSumColor(LightSource ls, int i, RayIntersection intersection, Point3D eye) {
				int intensity;
				double diffuseK;
				double reflectiveK;
				double nK = intersection.getKrn();
				if (i == 0) {
					intensity = ls.getR();
					diffuseK = intersection.getKdr();
					reflectiveK = intersection.getKrr();
				} else if (i == 1) {
					intensity = ls.getG();
					diffuseK = intersection.getKdg();
					reflectiveK = intersection.getKrg();
				} else {
					intensity = ls.getB();
					diffuseK = intersection.getKdb();
					reflectiveK = intersection.getKrb();
				}
				
				Point3D lPoint = ls.getPoint().sub(intersection.getPoint()).normalize();
				Point3D nPoint = intersection.getNormal().normalize();
				Point3D vPoint = intersection.getPoint().sub(eye).normalize();
				Point3D rPoint = nPoint.scalarMultiply(lPoint.scalarProduct(nPoint)*2).modifySub(lPoint).normalize();
				double cosinusDiffuse = lPoint.scalarProduct(nPoint);
				double cosinusReflected = rPoint.scalarProduct(vPoint);
				if (cosinusDiffuse < 0) {
					cosinusDiffuse = 0;
				}

				if (cosinusReflected > 0) {
					cosinusReflected = 0;
				} 
				
				double diffuse = intensity * diffuseK  *cosinusDiffuse; 
				double refl = intensity * reflectiveK * Math.pow(cosinusReflected, nK);
				short result = (short) (diffuse + refl);
				return result;
			}
			
			
		};

	}

}
