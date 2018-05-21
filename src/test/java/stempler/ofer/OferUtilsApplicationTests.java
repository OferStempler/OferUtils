package stempler.ofer;

public class OferUtilsApplicationTests {


	public static void permutation(String str) {
		permutation("", str);
	}

	private static void permutation(String prefix, String str) {
		int n = str.length();
		if (n == 0) {
			System.out.println(prefix);
		}
		else {
			for (int i = 0; i < n; i++)
				permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
		}
	}

private static void returnAllSubString(String str){
	int n = str.length();
}

	public static void main(String[] args) {


		OferUtilsApplicationTests.permutation("abcd");

//		Thread a = new Thread();
//		a.run();
//		Thread t1 = new Thread(new MyRun());
//		Thread t2 = new Thread(new MyRun());
//		Thread t3 =new Thread(new MyRun());
//
//		t1.run();
//		t2.run();
//		t3.run();
	}

}
