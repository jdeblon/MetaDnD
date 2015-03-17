import java.util.Random;


public class RNG {

	private Random m_random;

	public RNG() {
		this.m_random = new Random(System.currentTimeMillis());
	}

	public int GetRandomInteger(int min, int max){
		return this.m_random.nextInt((max - min) + 1) + min;
	}
}
