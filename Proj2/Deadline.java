import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

/**
 * A deadline for keep track of time.
 */
public class Deadline {
  public long deadline;
  
  /**
   * Constructs and sets the Deadline. 
   */
  public Deadline(long deadline) {
    this.deadline = deadline;
  }

  /**
   * Calculates and returns the remaining time until the Deadline must be met,
   * in ns.
   */
  long timeUntil() {
    ThreadMXBean bean = ManagementFactory.getThreadMXBean();
    long cpuTime = bean.isCurrentThreadCpuTimeSupported() ? bean.getCurrentThreadCpuTime() : 0;
    return deadline - cpuTime;
  }
}