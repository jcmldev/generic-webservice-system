
package dd;

import dd.output.LoggingContext;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

public class RoutingTable 
{
	private static final String PROC_NET_ROUTE_LOCATION = "/proc/net/route";
        //private static final String PROC_NET_ROUTE_LOCATION = "c:\\gf\\route.dat";
	
	private static final int ROUTE_UP_FLAG_MASK = 0x0001;
	
	
        /*
	public static RoutingEntry getRoute(InetAddress dstIp) {
		get();
		RoutingEntry rt = routes.get(dstIp);
		return rt;
	}
        * 
        */
        
        public static void writeOut(LoggingContext log, HashMap<InetAddress,RoutingEntry> routes)
        {
            InetAddress[]            ips = routes.keySet().toArray(new InetAddress[0]);
            InetAddress              ip;
            RoutingEntry             re;
            
            
            for(int i = 0; i < ips.length; i++)
            {
                ip = ips[i];
                re = routes.get(ip);
                
                log.log("Route ip:" + ip.getHostAddress() + " - isup: " + re.isUp + " - nexthop: " + re.nextHop + " - metric: " + re.metric);
            }
        
        }

        /*
         *             InetAddress ip = InetAddress.getByName("1.0.0.1");
            
            routes.put(ip, new RoutingEntry(ip, 2, Boolean.TRUE));
            
            
            ip = InetAddress.getByName("1.0.0.2");
            
            routes.put(ip, new RoutingEntry(ip, 3, Boolean.TRUE));
            
            ip = InetAddress.getByName("1.0.0.3");
            
            routes.put(ip, new RoutingEntry(ip, 1, Boolean.TRUE));
            
            return routes;

         */
        
	public static HashMap<InetAddress,RoutingEntry> get() throws FileNotFoundException, IOException {

            HashMap<InetAddress,RoutingEntry> routes = new HashMap<>();
            BufferedReader br = new BufferedReader(new FileReader(PROC_NET_ROUTE_LOCATION));
            String strLine;
	
            
            // Skip the first line output
            br.readLine();

            /* The output of /proc/net/route is in the following format:
                * 
                * 	Iface  Destination Gateway Flags RefCnt Use Metric Mask MTU Window IRTT 
                * 
                * where the first item in each line, when read by readLine() method, is a blank string.
                */
            while ((strLine = br.readLine()) != null) {
				//System.out.println("ROUTE RAW LINE: " + strLine);

                    if (strLine == null || strLine.length() == 0)
                            continue;

                    String [] fields = strLine.split("\\s+");

                    if (fields == null || fields.length < 11)
                            continue;

                    if ("Iface".equals(fields[0]))
                            continue;

                    String destinationHex = fields[1];
                    String gatewayHex = fields[2];
                    String flagHexStr = fields[3];
                    String metricStr = fields[6];

                    String dstIpStr = convertHexIpStrToIpString(destinationHex);
                    InetAddress dstIp = InetAddress.getByName(dstIpStr);

                    InetAddress nextHopIp = null;
                    if ("00000000".equals(gatewayHex)) {
                            nextHopIp = dstIp;
                    } else {
                            String nextHopIpStr = convertHexIpStrToIpString(gatewayHex);
                            nextHopIp = InetAddress.getByName(nextHopIpStr);
                    }

                    Boolean isRouteUp = false;
                    int flagInt = convertHexStrToInt(flagHexStr);
                    if ( (flagInt & ROUTE_UP_FLAG_MASK) == ROUTE_UP_FLAG_MASK) {
                            isRouteUp = true;
                    }

                    Integer metric = Integer.valueOf(metricStr);

                    RoutingEntry route = new RoutingEntry(nextHopIp, metric, isRouteUp);
                    routes.put(dstIp, route);


                }

                br.close();
        
                //writeOut(routes);
                
            return routes;
	}

	public static String convertHexIpStrToIpString(String hexIpString) {
		String s1 = hexIpString.substring(0, 2);
		String s2 = hexIpString.substring(2, 4);
		String s3 = hexIpString.substring(4, 6);
		String s4 = hexIpString.substring(6, 8);
		
		Integer a1 = Integer.valueOf(s4, 16);
		Integer a2 = Integer.valueOf(s3, 16);
		Integer a3 = Integer.valueOf(s2, 16);
		Integer a4 = Integer.valueOf(s1, 16);
		
		String decIpStr = new String("" + a1 + "." + a2 + "." + a3  + "." + a4);
		return decIpStr;
	}
	
	public static int convertHexStrToInt(String hexString) {
		Integer p = Integer.valueOf(hexString, 16);
		return p.intValue();
	}
}