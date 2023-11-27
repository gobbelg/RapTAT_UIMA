package src.main.gov.va.vha09.grecc.raptat.dw.sqlreadwrite.writer.impl;

public class Counter {

  private Integer initialValue;

  public Counter() {
    this(0);
  }


  protected Counter(Integer initialValue) {
    this.initialValue = initialValue;
  }


  public Integer next() {
    this.initialValue++;
    return this.initialValue;
  }

}
