package src.test.gov.va.vha09.grecc.raptat.gg.testing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class MyPointTester {
  public static void main(String[] args) {
    // ArrayList<Integer> testList = new ArrayList<Integer>(2);
    // testList.add(3);
    // testList.add(8);
    //
    // MyPointTester pointTester = new MyPointTester(testList);
    // System.out.println("Array is " + testList);
    // System.out.println("Point is " + pointTester);
    //
    // Integer yTester = pointTester.getY();
    // yTester = 12;
    // System.out.println("\nArray is now " + testList);
    // System.out.println("Point is now " + pointTester);
    //
    // testList.set(1, 45);
    // System.out.println("\nArray is now " + testList);
    // System.out.println("Point is now " + pointTester);
    //
    // Integer arrayInt = testList.get(0);
    // arrayInt = 63;
    // System.out.println("\nArray is now " + testList);
    // System.out.println("Point is no w" + pointTester);
    //
    // Integer first = 4, second = 7;
    // ArrayList<Integer> testList2 = new ArrayList<Integer>(2);
    // testList2.add(first);
    // testList2.add(second);
    // MyPointTester firstPoint = new MyPointTester(2,3);
    // firstPoint.setList(testList2);
    // MyPointTester copyPoint = new MyPointTester(firstPoint,testList2);
    // System.out.println("\nFirst point is " + firstPoint);
    // System.out.println("Copied point is " + copyPoint);

    // Integer third = 12, fourth = 25;
    // copyPoint.resetList(third, fourth);
    // System.out.println("\nFirst point is now " + firstPoint);
    // System.out.println("Copied point is now " + copyPoint);
    //
    // Integer testInteger = 16;
    // copyPoint.setY(testInteger);
    // System.out.println("\nFirst point is now " + firstPoint);
    // System.out.println("Copied point is now " + copyPoint);

    // testList2.set(0, 15);
    // System.out.println("\nFirst point is now " + firstPoint);
    // System.out.println("Copied point is now " + copyPoint);
    //
    // ArrayList<Integer> listFromFirstPoint = firstPoint.getPointList();
    // listFromFirstPoint.set(0, 102);
    // System.out.println("\nFirst point is now " + firstPoint);
    // System.out.println("Copied point is now " + copyPoint);

    Integer[] x1 = {23, 45, 88};
    Integer[] y1 = {55, 67, 72};
    Integer[] x2 = {116, 178, 165};
    Integer[] y2 = {101, 177, 199};

    MyPointTester pointA = new MyPointTester(x1, y1);
    MyPointTester pointB = new MyPointTester(x2, y2);

    Hashtable<String, MyPointTester> myHash = new Hashtable<>();

    myHash.put("a", pointA);
    myHash.put("b", pointB);

    System.out.println("a: " + pointA);
    MyPointTester aPoint = myHash.get("a");

    aPoint.x[0] = 35;

    System.out.println("a: " + pointA);

  }

  Integer[] x, y;


  public MyPointTester() {}


  public MyPointTester(ArrayList<Integer[]> aList) {
    this.x = aList.get(0);
    this.y = aList.get(1);
  }


  public MyPointTester(Integer[] x, Integer[] y) {
    this.x = x;
    this.y = y;
  }


  public MyPointTester(MyPointTester otherPoint, ArrayList<Integer[]> otherArray) {
    this.x = otherPoint.x;
    this.y = otherPoint.y;
  }


  public Integer[] getX() {
    return this.x;
  }


  public Integer[] getY() {
    return this.y;
  }


  public void setX(Integer[] x) {
    this.x = x;
  }


  public void setY(Integer[] y) {
    this.y = y;
  }


  @Override
  public String toString() {
    String result = "x: " + Arrays.toString(this.x) + ", y: " + Arrays.toString(this.y);
    return result;
  }
}
