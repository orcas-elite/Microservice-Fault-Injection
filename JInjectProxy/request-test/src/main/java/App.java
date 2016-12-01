import org.eclipse.jetty.server.Server;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        
        Server server = new Server(8080);
//        server.setHandler(new HelloWorld());
//
//        try {
//            server.start();
//			server.join();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        System.out.println( "Hello World!" );
    }
}
