package application;


import configuration.JwtRestConfiguration;
import filter.JwtRequestFilter;
import filter.JwtResourceFilter;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import resource.DataResource;
import resource.DummyResource;

public class JwtRestApplication extends Application<JwtRestConfiguration> {
	
	public static void main(String[] args) throws Exception{
		new JwtRestApplication().run(args);
		
	}

	@Override
	public void run(JwtRestConfiguration configuration, Environment env) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(configuration.getSecretKey());

		final DummyResource dummyResource = new DummyResource();
		final DataResource dataResource = new DataResource();
		env.jersey().register(JwtRequestFilter.class);
		
		env.jersey().register(dummyResource);
		env.jersey().register(dataResource);
		env.jersey().register(JwtResourceFilter.class);

	}

}
