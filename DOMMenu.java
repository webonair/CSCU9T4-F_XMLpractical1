import java.io.*;               // import input-output

import javax.xml.XMLConstants;
import javax.xml.parsers.*;         // import parsers
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.*;           // import XPath
import javax.xml.validation.*;      // import validators
import javax.xml.transform.*;       // import DOM source classes

//import com.sun.xml.internal.bind.marshaller.NioEscapeHandler;
import org.w3c.dom.*;               // import DOM

//I imported these
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
  DOM handler to read XML information, to create this, and to print it.

  @author   2721415		CSCU9T4, University of Stirling
  @version  22/03/21
*/
public class DOMMenu {

  /** Document builder */
  private static DocumentBuilder builder = null;

  /** XML document */
  private static Document document = null;

  /** XPath expression */
  private static XPath path = null;

  /** XML Schema for validation */
  private static Schema schema = null;

  /*----------------------------- General Methods ----------------------------*/

  /**
    Main program to call DOM parser.

    @param args         command-line arguments
  */
  public static void main(String[] args)  {
    // load XML file into "document"
    loadDocument(args[0]);
    
    // validate according to the provided schema
    if (validateDocument(args[1])) {
    	// print staff.xml using DOM methods and XPath queries
    	try { printNodes(); } catch (XPathException e) {
    		System.out.println("2721415 sucks at programming");
    	}  // I'm sure this exception won't happen.  It's just an obligation.
    }
  }

  /**
    Set global document by reading the given file.
    I didn't change anything here.

    @param filename     XML file to read
  */
  private static void loadDocument(String filename) {
    try {
      // create a document builder
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builder = builderFactory.newDocumentBuilder();

      // create an XPath expression
      XPathFactory xpathFactory = XPathFactory.newInstance();
      path = xpathFactory.newXPath();

      // parse the document for later searching
      document = builder.parse(new File(filename));
      
    }
    catch (Exception exception) {
      System.err.println("could not load document " + exception);
    }
  }

  /*-------------------------- DOM and XPath Methods -------------------------*/
  /**
   Validate the document given a schema file
   @param filename XSD file to read
  */
  private static Boolean validateDocument(String filename) {
    try {
      String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
      SchemaFactory factory = SchemaFactory.newInstance(language);
      schema = factory.newSchema(new File(filename));
      Validator validator = schema.newValidator();
      validator.validate(new DOMSource(document));
      return true;
    } catch (SAXParseException e) {  
    	/* 	Java requires that I catch every one of these potential exceptions.
    		However, it throws its own exceptions right at the start if the XML is wrong.
    		Anyway . . .  	*/
	    System.out.println("Line " + e.getLineNumber() + ": " + e.getMessage());
	    return false;
    } catch (IOException e2) {
    	System.out.println("Could not load schema.");
    	return false;
    } catch (SAXException e3) {
    	// I don't think the user would see this one.
    	System.out.println("SAX Exception.  It's my fault if this shows up.");
    	return false;
    }
  }
  /**
    Print nodes using DOM methods and XPath queries.
 * @throws XPathExpressionException 
  */
  private static void printNodes() throws XPathExpressionException {
    /*Node menu = document.getFirstChild();
    Node item = menu.getFirstChild().getNextSibling();
    System.out.println("First child is: " + menu.getNodeName());
    System.out.println("  Child is: " + item.getNodeName());*/
    
	//It doesn't throw an exception if "i" increments too many times, but it's ugly.
    System.out.println("~~~ Menu ~~~");
    for (int i = 1; i < 9; i++) {
    	String foodName = path.evaluate("/menu/item["+i+"]/name", document);
    	String foodPrice = path.evaluate("/menu/item["+i+"]/price", document);
    	String foodDesc = path.evaluate("/menu/item["+i+"]/description", document);
    	//Little trick from the Systems module
    	System.out.printf("%15s  £%5s   %s \n", foodName, foodPrice, foodDesc);
    }
  }

  /**
    Get result of XPath query.
    I didn't bother with this optional thing.  ;)

    @param query        XPath query
    @return         result of query
  */
  private static String query(String query) {
    String result = "";
    try {
      result = path.evaluate(query, document);
    }
    catch (Exception exception) {
      System.err.println("could not perform query - " + exception);
    }
    return(result);
  }
}
