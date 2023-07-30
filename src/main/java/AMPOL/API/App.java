package AMPOL.API;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.junit.Assert;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import dataProvider.ConfigFileReader;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.response.Response;

public class App 
{
	public static void main(String[] args) {
		
		ConfigFileReader configFileReader = ConfigFileReader.getInstance();		
		RestAssured.baseURI = configFileReader.getBaseUrl();
	    RequestSpecification httpRequest = RestAssured.given();
	    Response res = httpRequest.queryParam("key",configFileReader.getKeyValue())
	    			   .queryParam("q",configFileReader.getQ_Value()).queryParam("aqi", configFileReader.getAqi_Value()).get();
	    
	    Assert.assertEquals(res.getStatusCode(), 200);
	    
	    // First get the JsonPath object instance from the Response interface
		JsonPath jsonPathEvaluator = res.jsonPath();
		// Retrieve values from the response body
		String location_name = jsonPathEvaluator.get("location.name");
		String location_region = jsonPathEvaluator.get("location.region");
		String location_country = jsonPathEvaluator.get("location.country");
		String location_tz_id = jsonPathEvaluator.get("location.tz_id");
		String current_condition_text = jsonPathEvaluator.get("current.condition.text");
		int current_humidity = jsonPathEvaluator.get("current.humidity");
		float current_uv = jsonPathEvaluator.get("current.uv");		
		try {
				DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder(); 
				Document document = documentBuilder.newDocument();
				// root element
	            Element root = document.createElement("ase:aseXML");
	            // set attributes to the root element
	            Attr attr1 = document.createAttribute("xmlns:ase");
	            attr1.setValue("urn:aseXML:r41");
	            Attr attr2 = document.createAttribute("xmlns:xsi");
	            attr2.setValue("http://www.w3.org/2001/XMLSchema-instance");
	            Attr attr3 = document.createAttribute("xsi:schemaLocation");
	            attr3.setValue("urn:aseXML:r41 http://www.nemmco.com.au/aseXML/schemas/r41/aseXML_r41.xsd");
	            root.setAttributeNode(attr1);
	            root.setAttributeNode(attr2);
	            root.setAttributeNode(attr3);
	            	            
	            //Location element
	            Element Location = document.createElement("Location");
	            //Name element
	            Element Name = document.createElement("Name");
	            Name.appendChild(document.createTextNode(location_name));
	            Location.appendChild(Name);
	            //Region element
	            Element Region = document.createElement("Region");
	            Region.appendChild(document.createTextNode(location_region));
	            Location.appendChild(Region);
	            //Country element
	            Element Country = document.createElement("Country");
	            Country.appendChild(document.createTextNode(location_country));
	            Location.appendChild(Country);
	            //Country element
	            Element tz_id = document.createElement("tz_id");
	            tz_id.appendChild(document.createTextNode(location_tz_id));
	            Location.appendChild(tz_id);            
	            root.appendChild(Location);	            
	            //Current element
	            Element Current = document.createElement("Current");	            
	            //Condition element
	            Element Condition = document.createElement("Condition");            
	            //Text element
	            Element Text = document.createElement("Text");
	            Text.appendChild(document.createTextNode(current_condition_text));
	            Condition.appendChild(Text);
	            Current.appendChild(Condition);
	            //Humidity element
	            Element Humidity = document.createElement("Humidity");
	            Humidity.appendChild(document.createTextNode(String.valueOf(current_humidity)));
	            Current.appendChild(Humidity);
	            //uv element
	            Element uv = document.createElement("uv");
	            uv.appendChild(document.createTextNode(String.valueOf(current_uv)));
	            Current.appendChild(uv);	            
	            root.appendChild(Current);	            
	            document.appendChild(root);
	                        
	            //Transform the DOM Object to an XML file
	            TransformerFactory transformerFactory = TransformerFactory.newInstance();
	            Transformer transformer = transformerFactory.newTransformer();
	            DOMSource domSource = new DOMSource(document);
				StreamResult streamResult = new StreamResult(configFileReader.getXmlFilePath());     
	            transformer.transform(domSource, streamResult);
			
		} catch (ParserConfigurationException pce){
			pce.printStackTrace();
			
		} catch (TransformerException tfe) {
            tfe.printStackTrace();
        }	  
	}
}
