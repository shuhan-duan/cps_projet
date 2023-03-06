package componenet;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


import classes.ContentTemplate;
import connector.ContentManagementConector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.cps.p2Pcm.dataread.ContentDataManager;
import interfaces.ContentDescriptorI;
import interfaces.ContentTemplateI;
import ports.ContentManagementCIOutbound;

public class Client extends AbstractComponent {
    protected ContentManagementCIOutbound outportCM_client;
    protected String inportCM_facade;
    protected final int ID_TEMP = 0;

    protected Client(String ContentManagementInboudPort, String ContentManagementOutboudPort) throws Exception {
        super(1,0);
        outportCM_client = new ContentManagementCIOutbound(ContentManagementOutboudPort,this);
        outportCM_client.publishPort();
        inportCM_facade = ContentManagementInboudPort;
    }
    
    public ContentTemplate createTemplate(int numbre) throws ClassNotFoundException, IOException{
        ContentDataManager.DATA_DIR_NAME = "src/data";
        ArrayList<HashMap<String, Object>> result = ContentDataManager.readTemplates(numbre);
        Random rand = new Random();
		int randomIndex = rand.nextInt(result.size());
        HashMap<String, Object> res = result.get(randomIndex);
        ContentTemplate temp = new ContentTemplate(res);
        return temp;
    }
    
    public void doFind(ContentTemplateI temp) throws Exception {
    	//find
        System.out.println("\nplease find the template:\n "+ temp.toString());
        ContentDescriptorI res =this.outportCM_client.find(temp, 10); 
        if (res == null) {
        	System.out.println("\ncannot find !");
        }else { 
        	System.out.println("\nwe find :" + res.toString());
        }
	}
    
    public void doMatch(ContentTemplateI temp) throws Exception {
    	//match
        System.out.println("\nplease match the template:\n"+ temp.toString());
        
        Set<ContentDescriptorI> matched = new HashSet<>();
        matched =this.outportCM_client.match(temp,matched, 15); 
        if (matched == null) {
        	System.out.println("\n cannot match !");
        }else {
        	System.out.println("\nwe have matched: ");
        	for (ContentDescriptorI contentDescriptorI : matched) {
        		System.out.println("we matched :" + contentDescriptorI.toString());
			}
  		
        }
	}
    
    @Override
    public void start() throws ComponentStartException {
      try {
        super.start();
        this.doPortConnection(outportCM_client.getPortURI(), inportCM_facade, ContentManagementConector.class.getCanonicalName());
      } catch (Exception e) {
        throw new ComponentStartException(e);
      }
    }
    
    @Override
    public void execute() throws Exception {
      super.execute();
      Thread.sleep(2000);
      //choose template
      ContentTemplateI temp = createTemplate(ID_TEMP);
      System.out.println(temp.toString());
      //find
      //doFind(temp);
      //match
      doMatch(temp);
      
    }

}