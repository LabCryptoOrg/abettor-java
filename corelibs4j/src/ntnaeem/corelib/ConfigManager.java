package ntnaeem.corelib;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {

  private List<String> sections = new ArrayList<String>();
  private Map<String, List<String>> sectionLines = new HashMap<String, List<String>>();
  private BufferedReader reader;

  public ConfigManager(String filePath) throws IOException, SyntaxErrorConfigFile {
    openFile(filePath);
    parse();
  }

  public boolean hasSection(String section){
    return sections.contains(section);
  }

  public boolean hasAttribute(String attribute) {
    for(String section : sections){
      for(String line : sectionLines.get(section)){
        if(line.contains(attribute)){
          return true;
        }
      }
    }
    return false;
  }

  public List<String> getSectionLines(String sectionName){
    return sectionLines.get(sectionName);
  }

  public Map<String,String> getSectionAttributes(String sectionName){
    Map<String, String> attrValueMap = new HashMap<String , String>();
    List<String> lines = sectionLines.get(sectionName);
    for(String line : lines){
      String[] split = line.split(":");
      String attr = "";
      String value = "";
      attr = split[0].trim();
      if(split.length == 2) {
        value = split[1].trim();
      }
      attrValueMap.put(attr,value);
      //
    }
    return attrValueMap;
  }


  private void openFile(String filePath) throws FileNotFoundException {
    File file = new File(filePath);
    reader = new BufferedReader(new FileReader(file));
  }
  private void parse() throws SyntaxErrorConfigFile, IOException {
    List<String> lines = new ArrayList<>();
    String line;
    while ((line = reader.readLine()) != null) {
      line = line.trim();
      if (line.contains("[")) {
        if(sections.size() != 0){
          sectionLines.put(sections.get(sections.size() - 1) , lines);
          lines = new ArrayList<>();
        }
        sections.add(line.replace("[","").replace("]",""));

      } else {
        if(sections.size() == 0){
          throw new SyntaxErrorConfigFile("config parameter without section ");
        }else {
          if(line.length() != 0 &&
             line.charAt(0) != '#') {
            lines.add(line);
          }
        }
      }
    }
    if(sections.size() != 0){
      sectionLines.put(sections.get(sections.size() - 1) , lines);
    }
  }

//  public static void main(String[] args) throws IOException, SyntaxErrorConfigFile {
//    ConfigManager configManager = new ConfigManager("/opt/naeem/kawthar/hbc/server/hbc_service.conf");
//    System.out.println(configManager.hassection("GENERAL"));
//    System.out.println(configManager.hassection("SERVICE_LIST"));
//    System.out.println(configManager.hasAttribute("ip"));
//    Map<String, String> generalAttr = configManager.getsectionAttributes("GENERAL");
//    System.out.println(generalAttr.get("naeem-gate-ip"));
//
//  }

}
