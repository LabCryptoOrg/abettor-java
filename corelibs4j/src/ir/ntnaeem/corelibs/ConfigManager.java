package ir.ntnaeem.corelibs;


import java.io.*;
import java.util.*;

public class ConfigManager {
  private List<String> sections = new ArrayList<>();
  private Map<String, List<String>> sectionLines = new Hashtable<>();
  private Map<String, Map<String, String>> sectionParamValueMap = new Hashtable<>();
  private BufferedReader reader;
  public ConfigManager(String filePath) {
  }
  public boolean hasSection(String section){
    return sections.contains(section);
  }
  public boolean hasParameter(String section, String param) {
    if (!sectionParamValueMap.containsKey(section)) {
      return false;
    }
    Map<String, String> paramValueMap = sectionParamValueMap.get(section);
    if (!paramValueMap.containsKey(param)) {
      return false;
    }
    return true;
  }
  public String getValueAsString(String section, String param)
      throws ConfigurationException {
    if (!sectionParamValueMap.containsKey(section)) {
      throw new ConfigurationException("Section '" + section + "' can't be found.");
    }
    Map<String, String> paramValueMap = sectionParamValueMap.get(section);
    if (!paramValueMap.containsKey(param)) {
      throw new ConfigurationException("Parameter '" + section + "." + param + "' can't be found.");
    }
    return paramValueMap.get(param);
  }
  public int getValueAsInt(String section, String param)
      throws ConfigurationException {
    if (!sectionParamValueMap.containsKey(section)) {
      throw new ConfigurationException("Section '" + section + "' can't be found.");
    }
    Map<String, String> paramValueMap = sectionParamValueMap.get(section);
    if (!paramValueMap.containsKey(param)) {
      throw new ConfigurationException("Parameter '" + section + "." + param + "' can't be found.");
    }
    return Integer.parseInt(paramValueMap.get(param));
  }
  public long getValueAsLong(String section, String param)
      throws ConfigurationException {
    if (!sectionParamValueMap.containsKey(section)) {
      throw new ConfigurationException("Section '" + section + "' can't be found.");
    }
    Map<String, String> paramValueMap = sectionParamValueMap.get(section);
    if (!paramValueMap.containsKey(param)) {
      throw new ConfigurationException("Parameter '" + section + "." + param + "' can't be found.");
    }
    return Long.parseLong(paramValueMap.get(param));
  }
  public boolean getValueAsBoolean(String section, String param)
      throws ConfigurationException {
    if (!sectionParamValueMap.containsKey(section)) {
      throw new ConfigurationException("Section '" + section + "' can't be found.");
    }
    Map<String, String> paramValueMap = sectionParamValueMap.get(section);
    if (!paramValueMap.containsKey(param)) {
      throw new ConfigurationException("Parameter '" + section + "." + param + "' can't be found.");
    }
    return Boolean.parseBoolean(paramValueMap.get(param));
  }
  public List<String> getSectionLines(String section)
      throws ConfigurationException {
    if (!sectionParamValueMap.containsKey(section)) {
      throw new ConfigurationException("Section '" + section + "' can't be found.");
    }
    return sectionLines.get(section);
  }
  public void parseFile(String filePath)
      throws FileNotFoundException, ConfigurationException {
    openFile(filePath);
    parseFile(filePath);
  }
  private void openFile(String filePath) throws FileNotFoundException {
    File file = new File(filePath);
    reader = new BufferedReader(new FileReader(file));
  }
  private void parse()
      throws ConfigurationException, IOException {
    List<String> lines = new ArrayList<>();
    String line;
    String lastSectionName = null;
    while ((line = reader.readLine()) != null) {
      line = line.trim();
      if (line.contains("[")) {
        if(lastSectionName != null){
          sectionLines.put(lastSectionName , lines);
          lines.clear();
        }
        lastSectionName = line.replace("[","").replace("]","").trim();
        sections.add(lastSectionName);
      } else {
        if(lastSectionName == null){
          throw new ConfigurationException("A parameter was found that it has no section.");
        } else {
          if(line.length() != 0 && !line.startsWith("#")) {
            lines.add(line);
          }
        }
      }
    }
    if(lastSectionName != null){
      sectionLines.put(lastSectionName , lines);
    }
    for(String section : sections) {
      lines = sectionLines.get(section);
      for (String line2 : lines) {
        String[] split = line2.split("[:=]");
        String param;
        String value;
        param = split[0].trim();
        if (split.length == 2) {
          value = split[1].trim();
          if (!sectionParamValueMap.containsKey(section)) {
            sectionParamValueMap.put(section, new Hashtable<String, String>());
          }
          Map<String, String> paramValueMap = sectionParamValueMap.get(section);
          paramValueMap.put(param, value);
        }
      }
    }
  }
}
