package hr.fer.zemris.java.raytracer.model;

import static org.junit.Assert.*;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Sphere;
 
import org.junit.Test;
 
public class SphereTest {
        private static final double DELTA = 1e-9;
 
        @Test
        public void findClosestRayIntersection_ValidResult_Test() {
                Ray ray = Ray.fromPoints(new Point3D(), new Point3D(5, 5, 5));
                Sphere sphere = new Sphere(new Point3D(3.5, 3.5, 3.5), 2, 1, 1, 1, 0.5,
                                0.5, 0.5, 10);
                RayIntersection inter = sphere.findClosestRayIntersection(ray);
                assertTrue(inter.getKdr() == 1);
                assertTrue(inter.getKdg() == 1);
                assertTrue(inter.getKdb() == 1);
                assertTrue(inter.getKrr() == 0.5);
                assertTrue(inter.getKrg() == 0.5);
                assertTrue(inter.getKrb() == 0.5);
                System.out.println(inter.getPoint().x);
                System.out.println(inter.getPoint().y);
                System.out.println(inter.getPoint().z);
                assertEquals(inter.getPoint().x, 2.34529946162075, DELTA);
                assertEquals(inter.getPoint().y, 2.34529946162075, DELTA);
                assertEquals(inter.getPoint().z, 2.34529946162075, DELTA);
        }
 
        @Test
        public void ffindClosestRayIntersection_ValidResult_Test() {
                Ray ray = Ray.fromPoints(new Point3D(), new Point3D(1, 0, 0));
                Sphere sphere = new Sphere(new Point3D(0, 1, 0), 1, 1, 1, 1, 0.5, 0.5,
                                0.5, 10);
                RayIntersection inter = sphere.findClosestRayIntersection(ray);
 
                assertTrue(inter.getKdr() == 1);
                assertTrue(inter.getKdg() == 1);
                assertTrue(inter.getKdb() == 1);
                assertTrue(inter.getKrr() == 0.5);
                assertTrue(inter.getKrg() == 0.5);
                assertTrue(inter.getKrb() == 0.5);
                assertEquals(inter.getPoint().x, 0, DELTA);
                assertEquals(inter.getPoint().y, 0, DELTA);
                assertEquals(inter.getPoint().z, 0, DELTA);
        }
 
        @Test
        public void findClosestRayIntersection_NoIntersection_Test() {
                Ray ray = Ray.fromPoints(new Point3D(), new Point3D(5, 5, 5));
                Sphere sphere = new Sphere(new Point3D(10, 3.5, 3.5), 1, 1, 1, 1, 0.5,
                                0.5, 0.5, 10);
                RayIntersection inter = sphere.findClosestRayIntersection(ray);
                assertTrue(inter == null);
        }
 
}
