package online.shashki.rus.shashki.util;

/**
 * Created with IntelliJ IDEA.
 * Profile: alekspo
 * Date: 18.12.13
 * Time: 20:52
 */
public enum Operator
{

  ADDITION(PossibleOperators.ADD) {
    @Override
    public int apply(int x, int n) {
      return x + n;
    }
  },
  SUBTRACTION(PossibleOperators.SUB) {
    @Override
    public int apply(int x, int n) {
      return x - n;
    }
  };
  // You'd include other operators too...

  private final PossibleOperators operator;
//  private boolean inverted;

  private Operator(PossibleOperators operator) {
    this.operator = operator;
  }

  // Yes, enums *can* have abstract methods. This code compiles...
  public abstract int apply(int x, int n);

  public boolean equals(PossibleOperators operator) {
    return this.operator.equals(operator);
  }
}