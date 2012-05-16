package companyTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import browse.Browse;
import browse.KeyVal;

public class LoginTask {

	public static void main(String[] args) {
		int i=0;
		while(i++ < 100){
			Browse br = new Browse();
			Collection<KeyVal> data = new ArrayList<KeyVal>();
			if(new Random().nextBoolean()){
				data.add(KeyVal.createKV("username", "haidian"));
				data.add(KeyVal.createKV("password", "000000"));
			}else{
				data.add(KeyVal.createKV("username", "linxinghui"));
				data.add(KeyVal.createKV("password", "exam2410"));
			}
			try {
				br.post("http://127.0.0.1:8081/teach/login/doLogin",data );
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
