package atunibz.dperez.approject1617.xml;

/**
 * Factory class that provides a way to instantiate the two classes which handle the xml files on which this application IO is based. All instances of 
 * {@link atunibz.dperez.approject1617.xml.XMLUsers} and {@link atunibz.dperez.approject1617.xml.XMLTracks} are handed out by this class.
 * <p>
 * @author Davide Perez
 * @version 1.2
 * @since 21/5/2017
 */
public class XMLFactory {
	/**
	 * Enumerated type to restrict the input parameters to two only possible choices, each of which refers to either
	 * to the xml file holding the users information or the file holding the data.
	 */
	//define the type of the XML and restrict the invocation to two unique instance types
	public enum XMLType{
		/**
		 * xml file containing users information, refers to {@link atunibz.dperez.approject1617.xml.XMLUsers}
		 */
		USERS, 
		/**
		 * xml file containing data informtion, refers to {@link atunibz.dperez.approject1617.xml.XMLTracks}
		 */
		TRACKS
	}
	
	/**
	 * Default constructor made private to allow static invocation.
	 */
	private XMLFactory(){
		
	}
	/**
	 * Convenience static method for the constructor, used to allow static references to an XMLFactory object. Since the instances of this class
	 * are used exclusively to instantiate a class that works on the desired xml, this method allows an instance to be created and to be
	 * used without instantiating an object that becomes useless after a call. 
	 * @return a new XMLFactory instance
	 */
	public static XMLFactory newInstance(){
		return new XMLFactory();
	}
	
	/**
	 * Returns an instance of {@link atunibz.dperez.approject1617.xml.XMLTracks} or {@link atunibz.dperez.approject1617.xml.XMLUsers} , depending
	 * on the enum parameter.
	 * 
	 * @param type an instance of the enum XMLType
	 * @return the appropriate class between {@link atunibz.dperez.approject1617.xml.XMLUsers} and {@link atunibz.dperez.approject1617.xml.XMLTracks}
	 */
	public XMLWriter getXMLWriter(XMLType type){
		
		switch(type){
			case USERS: 
				return new XMLUsers();
			case TRACKS: 
				return new XMLTracks();
			default: 
				return null;
		}
	}
	
	

}
