
package dd.conversation;

import dd.output.LoggingContext;
import java.util.ArrayList;
import java.util.List;

public class MessageDependenceList 
{
    private static final String                             STRING_ITEM_DELIMITER = "---";
    
    private ArrayList<MessageDependenceListItem>            m_items = new ArrayList<>(); 
    

    public MessageDependenceList()
    {}

    public MessageDependenceList(ArrayList<MessageDependenceListItem> items)
    {
        m_items = items;
    }

    public void addDependency(String source, String target, int methodId)
    {
        MessageDependenceListItem            item = new MessageDependenceListItem(source, target, methodId);
    
        
        m_items.add(item);
    }
    
    public List<MessageDependenceListItem> getItems()
    {
        return m_items;
    }
    
    @Override
    public String toString()
    {
        String              string = "";
        
        
        for(MessageDependenceListItem item : m_items)
        {
            string += item.toString() + STRING_ITEM_DELIMITER;
        }
       
        if (string.length() > 0)
        {
            string = string.substring(0, string.length() - STRING_ITEM_DELIMITER.length());
        }
        
        return string;
    }

    public static MessageDependenceList fromString(String string)
    {
        ArrayList<MessageDependenceListItem>            items = new ArrayList<>();
        String[]                                        strings;
        String                                          item;
        MessageDependenceListItem                       messageDependenceListItem;
        MessageDependenceList                           messageDependenceList;
                
        
        if (string != null && string.length() > 0)
        {
            strings = string.split("\\" + STRING_ITEM_DELIMITER);

            for(int i = 0; i < strings.length; i++)
            {
                item = strings[i];

                if(item != null && item.length() > 0)
                {
                    messageDependenceListItem = MessageDependenceListItem.fromString(item);

                    if (item != null)
                    {
                        items.add(messageDependenceListItem);
                    }
                }
            }
            
            messageDependenceList = new MessageDependenceList(items);
        }
        else
        {
            messageDependenceList = new MessageDependenceList();
        }
        
        return messageDependenceList;
    }
    /*
    public DependenceGraph toDG()
    {
        String                  root;
        DependenceGraph         dg;

        
        if (m_items.isEmpty()) return null;
        
        root = m_items.get(0).getSource();
        dg = new DependenceGraph(root);

        for(MessageDependenceListItem item : m_items)
        {
            dg.addDependency(item.getSource(), item.getTarget());
        }
        
        return dg;
    }
    */
    public void writeOut(LoggingContext log)
    {
        log.log("MessageDependenceList - list of dependencies");
        
        for(MessageDependenceListItem item : m_items)
        {
            item.writeOut(log);
        }
    }
}