package atunibz.dperez.approject1617.xml;


import java.io.OutputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This interface provides methods to operate with {@code xml} files and perform basic operations in DOM,
 * such as adding elements and converting an element back to an Object. For an Object to be written to a {@code xml}
 * by using the appropriate method, it must declare to support such an operation by implementing the {@link XMLWritable}
 * interface.
 * @author Davide Perez
 * @version 1.5
 * @since 18/5/2017
 *
 */
public interface XMLWriter {
	/**
	 * Adds the element to a {@link Document}. The implementor must implement this method
	 * to perform only operations on the Document entity: the file IO must be handled
	 * by the {@link #write(OutputStream)} method implementation.
	 * @param entry an Object implementing the {@link XMLWritable} interface
	 */
	public void add(XMLWritable entry);//adds a new entry in xml. Input must be a XMLWritable object
	/**
	 * Formats the Document in an implementation-dependent way. The implementation does not
	 * take account of the Document content, but only regards the Document format (spaces, lines..).
	 */
	public void format();//structural format method
	/**
	 * Writes the {@link Document} to an {@link OutputStream}. All the IO aspects must be handled in the implementation of this method.
	 * @param output an OutputStream
	 */
	public void write(OutputStream output);
	/**
	 * Reads an {@link Element} into the corresponding Object. This is the complementary method of 
	 * {@link XMLWritable#toElement()} : the implementor must specify how the element and its associated
	 * entities are represented into an Object, by defining the Object class and setting its fields, which
	 * have to be defined by the Element structure.
	 * @param element the Element from which the Object is constructed
	 * @return an Object reflecting the Element
	 */
	public Object toObject(Element element);
	//public Document parse(File input);

}
