
package dd.graph;

/*
    mergers two DGs into one
    - the key is to assign node status such that it reflecs its best reachability
    -- that is; if the reachability differs in the two nodes the better known reachability is used in 
    --- order as specified by the enum elements
    - another aspect is to assign fl symptom information by merging information from the two
 */
public class DependenceGraphMerger 
{
    
    // returns the better reachability achieved
    public static DependenceGraph.ReachabilityStatus joinReachabilityStatus (
            DependenceGraph.ReachabilityStatus a, 
            DependenceGraph.ReachabilityStatus b)
    {
        if (a.compareTo(b) < 0)
        {
            return a;
        }
        else
        {
            return b;
        }
    }
    
    // joins the fl information
    // symptoms found have preference before symptoms not found
    public static DependenceGraph.FlStatus joinFlStatus (
            DependenceGraph.FlStatus a, 
            DependenceGraph.FlStatus b)
    {
        /*
         1 - if a or b is unknown - the other one is taken
         2 - if a or b is nofault - the other one is taken
         3 - now we know there is at least one fault thus:
             if a == b - a is taken
             otherwise element_and_transitive is taken
        */
        
        if (a == DependenceGraph.FlStatus.Unknown) 
            return b;
        
        if (b == DependenceGraph.FlStatus.Unknown) 
            return a;

        if (a == DependenceGraph.FlStatus.NoFault) 
            return b;
        
        if (b == DependenceGraph.FlStatus.NoFault) 
            return a;
        
        if (a == b) 
            return a;
        
        return DependenceGraph.FlStatus.ElementAndTransitiveFault;
    }

    public static DependenceGraph createDGUnion(DependenceGraph dgA, DependenceGraph dgB)
    {
        /*
         * steps
         * 1 - add all nodes from A
         * 2 - add all nodes from B not included yet
         * 3 - add all edges from A 
         * 4 - add all edges from B no included yet
         */
        
        DependenceGraph                     unionDG = new DependenceGraph(dgA.getRoot().getId());
        Node                                unionNode;
        Edge                                unionEdge;

        // add all nodes from A
        for (Node node : dgA.getNodes())
        {
            unionNode = unionDG.addNode(node.getId());
            unionNode.upgradeStatuses(node);
            unionNode.upgradeStatuses(dgB.getNode(node.getId()));
        }

        // add all nodes from B not included yet
        for (Node node : dgB.getNodes())
        {
            if (!unionDG.containsNode(node.getId()))
            {
                unionNode = unionDG.addNode(node.getId());
                unionNode.upgradeStatuses(node);
            }   
        }
        
        // add all edges from A
        for (Edge edge : dgA.getEdges())
        {
            unionEdge = unionDG.addEdge(
                    edge.getSource().getId(), 
                    edge.getTarget().getId());
            
            unionEdge.upgradeStatuses(edge);
            unionEdge.upgradeStatuses(dgB.getEdge(edge));
        }
        
        // add all edges from B no included yet
        for (Edge edge : dgB.getEdges())
        {
            unionEdge = unionDG.addEdge(
                    edge.getSource().getId(), 
                    edge.getTarget().getId());
            
            unionEdge.upgradeStatuses(edge);
        }
         
        return unionDG;
    }   
}
