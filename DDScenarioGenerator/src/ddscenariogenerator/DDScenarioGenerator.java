package ddscenariogenerator;

/*
 * Generates XML for configuration file - section <nodes>
 * - deployment of services to nodes
 * - dependencies as methods of services
 */

public class DDScenarioGenerator {

    

    // 2-6 size of dg
    public static void main(String[] args) {
    /*
        // service to node deployment assignment
        // first nodeId, followed by set of services deployed on that node
        int serviceToNodeDepoyment [][] = {
            {1, 1, 101, 201},
            {2, 2, 102, 202},
            {3, 3, 103, 203},
            {4, 4, 104, 204},
            {5, 5, 105, 205},
            {6, 106, 206},
            {7, 107, 207},
            {8, 108, 208},
            {9, 109, 209},
            {10, 110, 210},
            {11, 1, 101, 201},
            {12, 2, 102, 202},
            {13, 3, 103, 203},
            {14, 4, 104, 204},
            {15, 5, 105, 205},
            {16, 106, 206},
            {17, 107, 207},
            {18, 108, 208},
            {19, 109, 209},
            {20, 110, 210},
            {21, 1, 101, 201},
            {22, 2, 102, 202},
            {23, 3, 103, 203},
            {24, 4, 104, 204},
            {25, 5, 105, 205},
            {26, 106, 206},
            {27, 107, 207},
            {28, 108, 208},
            {29, 109, 209},
            {30, 110, 210},
            {31, 1, 101, 201},
            {32, 2, 102, 202},
            {33, 3, 103, 203},
            {34, 4, 104, 204},
            {35, 5, 105, 205},
            {36, 106, 206},
            {37, 107, 207},
            {38, 108, 208},
            {39, 109, 209},
            {40, 110, 210},
            {41, 1, 101, 201},
            {42, 2, 102, 202},
            {43, 3, 103, 203},
            {44, 4, 104, 204},
            {45, 5, 105, 205},
            {46, 106, 206},
            {47, 107, 207},
            {48, 108, 208},
            {49, 109, 209},
            {50, 110, 210},
        };
        */
        
        // !!! half of the nodes are used only !!!
        // service to node deployment assignment
        // first nodeId, followed by set of services deployed on that node
        int serviceToNodeDepoyment [][] = {
            {1, 1, 101, 201, 106, 206},
            {2, 2, 102, 202, 107, 207},
            {3, 3, 103, 203, 108, 208},
            {4, 4, 104, 204, 109, 209},
            {5, 5, 105, 205, 110, 210},
            {6},
            {7},
            {8},
            {9},
            {10},
            {11, 1, 101, 201, 106, 206},
            {12, 2, 102, 202, 107, 207},
            {13, 3, 103, 203, 108, 208},
            {14, 4, 104, 204, 109, 209},
            {15, 5, 105, 205, 110, 210},
            {16},
            {17},
            {18},
            {19},
            {20},
            {21, 1, 101, 201, 106, 206},
            {22, 2, 102, 202, 107, 207},
            {23, 3, 103, 203, 108, 208},
            {24, 4, 104, 204, 109, 209},
            {25, 5, 105, 205, 110, 210},
            {26},
            {27},
            {28},
            {29},
            {30},
            {31, 1, 101, 201, 106, 206},
            {32, 2, 102, 202, 107, 207},
            {33, 3, 103, 203, 108, 208},
            {34, 4, 104, 204, 109, 209},
            {35, 5, 105, 205, 110, 210},
            {36},
            {37},
            {38},
            {39},
            {40},
            {41, 1, 101, 201, 106, 206},
            {42, 2, 102, 202, 107, 207},
            {43, 3, 103, 203, 108, 208},
            {44, 4, 104, 204, 109, 209},
            {45, 5, 105, 205, 110, 210},
            {46},
            {47},
            {48},
            {49},
            {50},
        };

        
        // service dependencies
        // dependent service, dependent service method, antecedent service, antecedent service method
        
/*
        //DG size 1
        int dependencies [][] = {
        };
*/
  
        
        /*
        //DG size 2
        int dependencies [][] = {
            {1, 0, 102, 0},
            {1, 1, 103, 1},
            {2, 0, 103, 0},
            {2, 1, 104, 1},
            {3, 0, 104, 0},
            {3, 1, 105, 1},
            {4, 0, 105, 0},
            {4, 1, 101, 1},
            {5, 0, 101, 0},
            {5, 1, 102, 1},
        };
        */

        /*
        //DG size 3
        int dependencies [][] = {
            {1, 0, 102, 0},
            {1, 1, 103, 1},
            {2, 0, 103, 0},
            {2, 1, 104, 1},
            {3, 0, 104, 0},
            {3, 1, 105, 1},
            {4, 0, 105, 0},
            {4, 1, 101, 1},
            {5, 0, 101, 0},
            {5, 1, 102, 1},
            {101, 0, 106, 0},
            {101, 1, 107, 1},
            {102, 0, 107, 0},
            {102, 1, 108, 1},
            {103, 0, 108, 0},
            {103, 1, 109, 1},
            {104, 0, 109, 0},
            {104, 1, 110, 1},
            {105, 0, 110, 0},
            {105, 1, 106, 1}
        };
        */

        //DG size 4
        int dependencies [][] = {
            {1, 0, 102, 0},
            {1, 1, 103, 1},
            {2, 0, 103, 0},
            {2, 1, 104, 1},
            {3, 0, 104, 0},
            {3, 1, 105, 1},
            {4, 0, 105, 0},
            {4, 1, 101, 1},
            {5, 0, 101, 0},
            {5, 1, 102, 1},
            {101, 0, 106, 0},
            {101, 1, 107, 1},
            {102, 0, 107, 0},
            {102, 1, 108, 1},
            {103, 0, 108, 0},
            {103, 1, 109, 1},
            {104, 0, 109, 0},
            {104, 1, 110, 1},
            {105, 0, 110, 0},
            {105, 1, 106, 1},
            {101, 0, 202, 0},
            {101, 1, 203, 1},
            {102, 0, 203, 0},
            {102, 1, 204, 1},
            {103, 0, 204, 0},
            {103, 1, 205, 1},
            {104, 0, 205, 0},
            {104, 1, 201, 1},
            {105, 0, 201, 0},
            {105, 1, 202, 1}
        };

        /*
        //DG size 5
        int dependencies [][] = {
            {1, 0, 102, 0},
            {1, 1, 103, 1},
            {2, 0, 103, 0},
            {2, 1, 104, 1},
            {3, 0, 104, 0},
            {3, 1, 105, 1},
            {4, 0, 105, 0},
            {4, 1, 101, 1},
            {5, 0, 101, 0},
            {5, 1, 102, 1},
            {101, 0, 106, 0},
            {101, 1, 107, 1},
            {102, 0, 107, 0},
            {102, 1, 108, 1},
            {103, 0, 108, 0},
            {103, 1, 109, 1},
            {104, 0, 109, 0},
            {104, 1, 110, 1},
            {105, 0, 110, 0},
            {105, 1, 106, 1},
            {101, 0, 202, 0},
            {101, 1, 203, 1},
            {102, 0, 203, 0},
            {102, 1, 204, 1},
            {103, 0, 204, 0},
            {103, 1, 205, 1},
            {104, 0, 205, 0},
            {104, 1, 201, 1},
            {105, 0, 201, 0},
            {105, 1, 202, 1},
            {1, 0, 206, 0},
            {1, 1, 207, 1},
            {2, 0, 207, 0},
            {2, 1, 208, 1},
            {3, 0, 208, 0},
            {3, 1, 209, 1},
            {4, 0, 209, 0},
            {4, 1, 210, 1},
            {5, 0, 210, 0},
            {5, 1, 206, 1}
        };
*/
        /*
                //DG size 6
        int dependencies [][] = {
            {1, 0, 102, 0},
            {1, 1, 103, 1},
            {2, 0, 103, 0},
            {2, 1, 104, 1},
            {3, 0, 104, 0},
            {3, 1, 105, 1},
            {4, 0, 105, 0},
            {4, 1, 101, 1},
            {5, 0, 101, 0},
            {5, 1, 102, 1},
            {101, 0, 106, 0},
            {101, 1, 107, 1},
            {102, 0, 107, 0},
            {102, 1, 108, 1},
            {103, 0, 108, 0},
            {103, 1, 109, 1},
            {104, 0, 109, 0},
            {104, 1, 110, 1},
            {105, 0, 110, 0},
            {105, 1, 106, 1},
            {101, 0, 202, 0},
            {101, 1, 203, 1},
            {102, 0, 203, 0},
            {102, 1, 204, 1},
            {103, 0, 204, 0},
            {103, 1, 205, 1},
            {104, 0, 205, 0},
            {104, 1, 201, 1},
            {105, 0, 201, 0},
            {105, 1, 202, 1},
            {1, 0, 206, 0},
            {1, 1, 207, 1},
            {2, 0, 207, 0},
            {2, 1, 208, 1},
            {3, 0, 208, 0},
            {3, 1, 209, 1},
            {4, 0, 209, 0},
            {4, 1, 210, 1},
            {5, 0, 210, 0},
            {5, 1, 206, 1},
            {106, 0, 207, 0},
            {106, 1, 208, 1},
            {107, 0, 208, 0},
            {107, 1, 209, 1},
            {108, 0, 209, 0},
            {108, 1, 210, 1},
            {109, 0, 210, 0},
            {109, 1, 206, 1},
            {110, 0, 206, 0},
            {110, 1, 207, 1}
        };
*/
        
        generateNodesSection(serviceToNodeDepoyment, dependencies);
    }

    
    
    public static void main1(String[] args) {
    
        // service to node deployment assignment
        // first nodeId, followed by set of services deployed on that node
        int serviceToNodeDepoyment [][] = {
            {1, 1, 101, 201},
            {2, 2, 102, 202},
            {3, 3, 103, 203},
            {4, 4, 104, 204},
            {5, 5, 105, 205},
            {6},
            {7},
            {8},
            {9},
            {10},
            {11, 1, 101, 201},
            {12, 2, 102, 202},
            {13, 3, 103, 203},
            {14, 4, 104, 204},
            {15, 5, 105, 205},
            {16},
            {17},
            {18},
            {19},
            {20},
            {21, 1, 101, 201},
            {22, 2, 102, 202},
            {23, 3, 103, 203},
            {24, 4, 104, 204},
            {25, 5, 105, 205},
            {26},
            {27},
            {28},
            {29},
            {30},
            {31, 1, 101, 201},
            {32, 2, 102, 202},
            {33, 3, 103, 203},
            {34, 4, 104, 204},
            {35, 5, 105, 205},
            {36},
            {37},
            {38},
            {39},
            {40},
            {41, 1, 101, 201},
            {42, 2, 102, 202},
            {43, 3, 103, 203},
            {44, 4, 104, 204},
            {45, 5, 105, 205},
            {46},
            {47},
            {48},
            {49},
            {50}
        };
        
        
        // service dependencies
        // dependent service, dependent service method, antecedent service, antecedent service method
        
/*
        //DG size 1
        int dependencies [][] = {
        };
*/
  
        
        /*
        //DG size 2
        int dependencies [][] = {
            {1, 0, 102, 0},
            {1, 1, 103, 1},
            {2, 0, 103, 0},
            {2, 1, 104, 1},
            {3, 0, 104, 0},
            {3, 1, 105, 1},
            {4, 0, 105, 0},
            {4, 1, 101, 1},
            {5, 0, 101, 0},
            {5, 1, 102, 1},
        };
        */

        /*
        //DG size 3
        int dependencies [][] = {
            {1, 0, 102, 0},
            {1, 1, 103, 1},
            {2, 0, 103, 0},
            {2, 1, 104, 1},
            {3, 0, 104, 0},
            {3, 1, 105, 1},
            {4, 0, 105, 0},
            {4, 1, 101, 1},
            {5, 0, 101, 0},
            {5, 1, 102, 1},
            {101, 0, 202, 0},
            {101, 1, 203, 1},
            {102, 0, 203, 0},
            {102, 1, 204, 1},
            {103, 0, 204, 0},
            {103, 1, 205, 1},
            {104, 0, 205, 0},
            {104, 1, 201, 1},
            {105, 0, 201, 0},
            {105, 1, 202, 1}
        };
        */

        
        //DG size 4
        int dependencies [][] = {
            {1, 0, 102, 0},
            {1, 1, 103, 1},
            {2, 0, 103, 0},
            {2, 1, 104, 1},
            {3, 0, 104, 0},
            {3, 1, 105, 1},
            {4, 0, 105, 0},
            {4, 1, 101, 1},
            {5, 0, 101, 0},
            {5, 1, 102, 1},
            {101, 0, 204, 0},
            {101, 1, 205, 1},
            {102, 0, 205, 0},
            {102, 1, 201, 1},
            {103, 0, 201, 0},
            {103, 1, 202, 1},
            {104, 0, 202, 0},
            {104, 1, 203, 1},
            {105, 0, 203, 0},
            {105, 1, 204, 1},
            {1, 0, 203, 0},
            {1, 1, 204, 1},
            {2, 0, 204, 0},
            {2, 1, 205, 1},
            {3, 0, 205, 0},
            {3, 1, 201, 1},
            {4, 0, 201, 0},
            {4, 1, 202, 1},
            {5, 0, 202, 0},
            {5, 1, 203, 1}
        };

        generateNodesSection(serviceToNodeDepoyment, dependencies);
    }
    
    private static void generateNodesSection(int serviceToNodeDepoyment [][], int dependencies [][])
    {
        int nodeId;
        int serviceId;
        
        
        writeOut("");
        writeOut("");
        writeOut("");
        
        nodesStart();
    
        // for each node hosting some services
        for(int nodeServices[] : serviceToNodeDepoyment)
        {
            nodeId = nodeServices[0];
            nodeStart(nodeId);
            
            // for each service
            for (int i = 1; i < nodeServices.length;i++)
            {
                serviceId = nodeServices[i];
                serviceStart(serviceId);
                
                // methods 0 and 1
                generateServiceMethod(serviceId, 0, dependencies);
                generateServiceMethod(serviceId, 1, dependencies);
                
                serviceEnd();
            }
        
            nodeEnd();
        }
        
        nodesEnd();
        
        writeOut("");
        writeOut("");
        writeOut("");
    }
    
    private static void generateServiceMethod(int serviceId, int serviceMethodIndex, int dependencies [][])
    {
        methodStart();
        
        for(int dependence[] : dependencies)
        {
            if (dependence[0] == serviceId && dependence[1] == serviceMethodIndex)
            {
                stepStart(dependence[2], dependence[3]);
            }
        }
        
        methodEnd();
    }
    
    private static void writeOut(String line)
    {
        System.out.println(line);
    }
    
    private static void nodesStart()
    {
        writeOut("<nodes>");
    }

    private static void nodesEnd()
    {
        writeOut("</nodes>");
    }
    
    private static void nodeStart(int nodeId)
    {
        writeOut("<node ip=\"10.0.0." + nodeId + "\" hasGpService=\"true\" dependenceDataProviderRespondsWithFault=\"false\">");
    }

    private static void nodeEnd()
    {
        writeOut("</node>");
    }

    private static void serviceStart(int serviceId)
    {
        writeOut("<service name=\"WS" + serviceId + "\">");
    }

    private static void serviceEnd()
    {
        writeOut("</service>");
    }
    
    private static void methodStart()
    {
        writeOut("<method startDelayDistributionStart=\"10\" startDelayDistributionEnd=\"20\" endDelayDistributionStart=\"10\" endDelayDistributionEnd=\"20\">");
    }

    private static void methodEnd()
    {
        writeOut("</method>");
    }

    private static void stepStart(int targetServiceId, int targetMethodIndex)
    {
        writeOut("<step endDelayDistributionStart=\"10\" endDelayDistributionEnd=\"20\" targetMethod=\"" + targetMethodIndex + "\" targetService=\"WS" + targetServiceId + "\"/>");
    }

}
