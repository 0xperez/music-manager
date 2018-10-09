package atunibz.dperez.approject1617.xml;

import org.w3c.dom.Element;

/**
 * A class implements the XMLWritable to indicate that it is legal to make an instance of that class
 * persistent by writing it on a {@linkplain org.w3c.dom.Document Document}, which can then be transformed 
 * in a {@code xml} file. For an object to be written on a {@code xml} it is necessary to have an equivalent {@link Element} representation of it.
 * Such a representation is defined by an implementation of the {@link #toElement()} method.
 * @author Davide Perez
 * @since 19/5/2017
 * @version 1.0
 *
 */
public interface XMLWritable {
	/**
	 * Defines the way in which an Object is written to an {@code xml} by specifying
	 * how the Object is represented into an {@link Element}. The choice of the Element 
	 * structure (attributes, children elements...) is up to the implementor, but any
	 * implementation must return an Element which is valid against the {@code xml} syntax and 
	 * reflects the structure of the Object to which it is referred.
	 * @return an Element representation of the Object
	 */
	public Element toElement(); 

}
