package ir.ntnaeem.corelibs;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
  private List<String> sections = new ArrayList<>();
  private Map<String, List<String>> sectionLines = new HashMap<>();
  private Map<String, Map<String, String>> sectionParamValueMap = new HashMap<>();
  private BufferedReader reader;
  public ConfigManager(String filePath) {
  }
  public boolean hasSection(String section){
    return sections.contains(section);
  }
  public boolean hasParameter(String section, String param) {
    if (!sectionParamValueMap.containsKey(section)) {
      // throw new RuntimeException("Section '" + section + "' can't be found.");
      return false;
    }
    Map<String, String> paramValueMap = sectionParamValueMap.get(section);
    if (!paramValueMap.containsKey(param)) {
      // throw new RuntimeException("Parameter '" + section + "." + param + "' can't be found.");
      return false;
    }
    return true;
  }
  public String getValueAsString(String section, String param) {
    if (!sectionParamValueMap.containsKey(section)) {
      throw new RuntimeException("Section '" + section + "' can't be found.");
    }
    Map<String, String> paramValueMap = sectionParamValueMap.get(section);
    if (!paramValueMap.containsKey(param)) {
      throw new RuntimeException("Parameter '" + section + "." + param + "' can't be found.");
    }
    return paramValueMap.get(param);
  }
  public int getValueAsInt(String section, String param) {
    if (!sectionParamValueMap.containsKey(section)) {
      throw new RuntimeException("Section '" + section + "' can't be found.");
    }
    Map<String, String> paramValueMap = sectionParamValueMap.get(section);
    if (!paramValueMap.containsKey(param)) {
      throw new RuntimeException("Parameter '" + section + "." + param + "' can't be found.");
    }
    return Integer.parseInt(paramValueMap.get(param));
  }
  public long getValueAsLong(String section, String param) {
    if (!sectionParamValueMap.containsKey(section)) {
      throw new RuntimeException("Section '" + section + "' can't be found.");
    }
    Map<String, String> paramValueMap = sectionParamValueMap.get(section);
    if (!paramValueMap.containsKey(param)) {
      throw new RuntimeException("Parameter '" + section + "." + param + "' can't be found.");
    }
    return Long.parseLong(paramValueMap.get(param));
  }
  public boolean getValueAsBoolean(String section, String param) {
    if (!sectionParamValueMap.containsKey(section)) {
      throw new RuntimeException("Section '" + section + "' can't be found.");
    }
    Map<String, String> paramValueMap = sectionParamValueMap.get(section);
    if (!paramValueMap.containsKey(param)) {
      throw new RuntimeException("Parameter '" + section + "." + param + "' can't be found.");
    }
    return Boolean.parseBoolean(paramValueMap.get(param));
  }
  public List<String> getSectionLines(String sectionName){
    return sectionLines.get(sectionName);
  }
  public void parseFile(String filePath)
      throws FileNotFoundException {
    openFile(filePath);
    parseFile(filePath);
  }
  private void openFile(String filePath) throws FileNotFoundException {
    File file = new File(filePath);
    reader = new BufferedReader(new FileReader(file));
  }
  private void parse()
      throws SyntaxErrorConfigFile, IOException {
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
          throw new SyntaxErrorConfigFile("A parameter was found that it has no section.");
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
    for(String section : sections) {
      lines = sectionLines.get(section);
      for (String line2 : lines) {
        String[] split = line2.split("[:=]");
        String param;
        String value = "";
        param = split[0].trim();
        if (split.length == 2) {
          value = split[1].trim();
          paramValueMap.put(param, value);
        }
      }
    }
  }
}
