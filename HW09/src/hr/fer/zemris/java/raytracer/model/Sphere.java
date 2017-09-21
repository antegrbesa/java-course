package hr.fer.zemris.java.raytracer.model;

/**
 * Represents a {@link GraphicalObject} sphere. 
 * @author Ante
 *
 */
public class Sphere extends GraphicalObject {
	/**Center point*/
	private Point3D center;
	/**Radius*/
	private double radius;
	
	double kdr;
	double kdg;
	double kdb; 
	double krr; 
	double krg; 
	double krb; 
	double krn;
	
	
	
	/**
	 * Creates a sphere with specified arguments. 
	 * @param center
	 * @param radius
	 * @param kdr
	 * @param kdg
	 * @param kdb
	 * @param krr
	 * @param krg
	 * @param krb
	 * @param krn
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		double root = Math.pow(ray.direction.scalarProduct(ray.start.sub(center)),2)
                - Math.pow((ray.start.sub(center).norm()), 2) + radius * radius;
		
		if (root < 0) {
			return null;
		}
		
		double firstFinalExpr = - ray.direction.scalarProduct(ray.start.sub(center));
		
		if (root < 1E-9) {	//approx. 0
			Point3D point = ray.start.add(ray.direction.scalarMultiply(firstFinalExpr));
			return new RayIntersectionImpl(point, firstFinalExpr, true);
		}
		
		double finalResult1 = firstFinalExpr + Math.sqrt(root);
		double finalResult2 = firstFinalExpr - Math.sqrt(root);
		
		double result = Math.min(finalResult1, finalResult2);
		
		Point3D point = ray.start.add(ray.direction.scalarMultiply(result));
		return new RayIntersectionImpl(point, result, true);
	}

	/**
	 * Implementation of a {@link RayIntersection}.
	 * @author Ante
	 *
	 */
	protected class RayIntersectionImpl extends RayIntersection {
		
		/**
		 * Constructor for this clas
		 * @param point point to set
		 * @param distance distance to set
		 * @param outer true if outer intersection, false otherwise
		 */
		protected RayIntersectionImpl(Point3D point, double distance, boolean outer) {
			super(point, distance, outer);
		}

		@Override
		public Point3D getNormal() {
			Point3D point = super.getPoint();
			Point3D normal = point.sub(center);
			return normal.normalize();
		}

		@Override
		public double getKdr() {
			return kdr;
		}

		@Override
		public double getKdg() {
			return kdg;
		}

		@Override
		public double getKdb() {
			return kdb;
		}

		@Override
		public double getKrr() {
			return krr;
		}

		@Override
		public double getKrg() {
			return krg;
		}

		@Override
		public double getKrb() {
			return krb;
		}

		@Override
		public double getKrn() {
			return krn;
		}
		
	}
	
}
