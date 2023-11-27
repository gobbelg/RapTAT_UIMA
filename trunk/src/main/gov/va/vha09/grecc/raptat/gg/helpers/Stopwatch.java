/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.helpers;

/**************************************************
 *
 *
 * @author vhatvhgobbeg - Feb 21, 2013
 **************************************************/
public class Stopwatch {
  private long ticTime = 0L;

  private long totalDuration = 0L;
  private long mark = 0L;
  private boolean isRunning = false;


  public Stopwatch() {
    this.tic();
  }


  public long getSecondsSinceMark() {
    return System.currentTimeMillis() - mark;
  }


  public long getSecondsSinceMark(String event) {
    long milliseconds = this.getSecondsSinceMark() - ticTime;
    System.out.println("Time " + event + ":" + milliseconds + "ms");
    return milliseconds;
  }


  // Call this if you want the amount of time since tic() called but do not
  // wish to pause the timer like when toc() is called
  public long getSecondsSinceTic() {
    return (System.currentTimeMillis() - ticTime) / 1000;
  }


  /*************************************************************
   * @return ticTime
   *
   * @author Glenn Gobbel - Feb 5, 2014
   *************************************************************/
  public long getTicTime() {
    return ticTime;
  }


  /*************************************************************
   * @return totalDuration - all time intervals summed for calls to tic() followed by toc()
   *
   * @author Glenn Gobbel - Feb 5, 2014
   *************************************************************/
  public long getTotalDuration() {
    return totalDuration;
  }


  /*************************************************************
   * Mark the time that this method is called and store it in the field "mark". Retrieve time since
   * this call with call to getSecondsSinceMark().
   *
   * @author Glenn Gobbel - Feb 5, 2014
   *************************************************************/
  public void markTime() {
    this.markTime("");
  }


  /*************************************************************
   * Mark the time that this method is called and store it in the field "mark". Print the marked
   * time to console. Retrieve time since this call with call to getSecondsSinceMark().
   *
   * @author Glenn Gobbel - Feb 5, 2014
   *************************************************************/
  public void markTime(String event) {
    mark = System.currentTimeMillis() - ticTime;;
    if (event.length() > 0) {
      System.out.println("\nMarked time at:" + mark + "  Event:" + event);
    }
  }


  /*************************************************************
   * @param event
   *
   * @author Glenn Gobbel - Feb 5, 2014
   *************************************************************/
  public void reportTotalDuration(String event) {
    System.out.println(event + " Total Duration:" + totalDuration + " msec");
  }


  /*************************************************************
   * Return all parameters to their initial state. All previous timings stored by the instance of
   * this Stopwatch are lost when this is called.
   *
   * @author Glenn Gobbel - Feb 5, 2014
   *************************************************************/
  public void reset() {
    ticTime = 0L;
    totalDuration = 0L;
    mark = 0L;
  }


  /*************************************************************
   * @param ticTime
   *
   * @author Glenn Gobbel - Feb 5, 2014
   *************************************************************/
  public void setTicTime(long ticTime) {
    this.ticTime = ticTime;
  }


  /*************************************************
   * Starts the Stopwatch instance, and sets the marked time to the time that the Stopwatch instance
   * was started. Duration is printed in response to toc().
   *
   * @param event - String representing the event being timed
   * @author Glenn T. Gobbel - Dec 14, 2011
   *************************************************/
  public void tic() {
    this.tic("");
  }


  /*************************************************
   * Starts the Stopwatch instance and prints the event to console.
   *
   * @param event - String representing the event being timed
   * @author Glenn T. Gobbel - Dec 14, 2011
   *************************************************/
  public void tic(String event) {
    ticTime = System.currentTimeMillis();
    isRunning = true;

    if (event.length() > 0) {
      System.out.println("\nTimer Started:  " + event);
    }
  }


  /*************************************************************
   * Stops timer and adds the time interval between last call to tic() and this call to toc()
   *
   * @param textString
   * @throws ImproperClassState
   *
   * @author Glenn Gobbel - Feb 5, 2014
   *************************************************************/
  public void toc() {
    if (isRunning) {
      long duration = System.currentTimeMillis() - ticTime;
      // System.out.println( " - Process Duration: " + duration +
      // " milliseconds" );
      totalDuration += duration;
      isRunning = false;
    }

  }


  /*************************************************************
   * Pauses timer and adds the time interval between last call to tic() and this call to toc()
   *
   * @param textString
   * @throws ImproperClassState
   *
   * @author Glenn Gobbel - Feb 5, 2014
   *************************************************************/
  public void toc(String textString) {
    this.toc();
    System.out.println(textString + ", Time:" + totalDuration + " milliseconds");
  }


  public static void main(String[] args) {
    Stopwatch timer = new Stopwatch();

    int i = 0;
    while (i < 1000) {
      System.out.println("Duration is " + timer.getSecondsSinceTic());
      i++;
    }
  }

}
