import java.security.InvalidParameterException;

public class Resizer {

	public static Resolution currentResolution = Client.clientResolution;

	public int setX(int x) {
		if (x < 0 || x > 1366) {
			throw new InvalidParameterException("Original x value must be below HD resolution");
		}
		switch (currentResolution) {
			case HD:
				return x;
			case FULLHD:
			case QUADHD:
			default:
				throw new InvalidParameterException("Unable to render new X resolution");
		}
	}

	public int setY(int y) {
		if (y < 0 || y > 768) {
			throw new InvalidParameterException("Original y value must be below HD resolution");
		}
		switch (currentResolution) {
			case HD:
				return y;
			case FULLHD:
			case QUADHD:
			default:
				throw new InvalidParameterException("Unable to render new Y resolution");
		}
	}
}
