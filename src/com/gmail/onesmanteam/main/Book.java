package com.gmail.onesmanteam.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@SuppressWarnings("unused")
public class Book {

	private String xmlFilePath;
	private String name;
	private String author;
	private String genre;
	private String length;
	private String reader;
	private String curCover;
	private String description;
	private String serie;

	private String[] genres;
	
	private List<String> bookCoverPaths = new ArrayList<String>();

	public Book(String xmlFilePath) {
		this.xmlFilePath = xmlFilePath;
		readXml();
	}

	public Book(String name, String author, String description, String genre, String length, String serie) {
		this.name = name;
		this.author = author;
		this.genre = genre;
		this.length = length;
		this.description = description;
		this.serie = serie;
	}

	public Book(String name, String author, String description, String genre, String length, String reader, String serie) {
		this.name = name;
		this.author = author;
		this.genre = genre;
		this.length = length;
		this.reader = reader;
		this.description = description;
		this.serie = serie;
	}

	public void readXml() {
		File xml = new File(xmlFilePath);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbf.newDocumentBuilder();
			Document doc = dBuilder.parse(xml);

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("book");
			Node nNode = nList.item(0);

			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;

				name = eElement.getElementsByTagName("title").item(0).getTextContent();
				author = eElement.getElementsByTagName("author").item(0).getTextContent();
//				genre = eElement.getElementsByTagName("genre").item(0).getTextContent();
				length = eElement.getElementsByTagName("length").item(0).getTextContent();
				description = eElement.getElementsByTagName("description").item(0).getTextContent();
				serie = eElement.getElementsByTagName("serie").item(0).getTextContent();
				
				if(description.equals("")){
					description = "No description available, add one in the XML file";
				}

				Element cElement = (Element) eElement.getElementsByTagName("covers").item(0);
				Element gElement = (Element) eElement.getElementsByTagName("genres").item(0);
				genres = new String[gElement.getChildNodes().getLength()];
				for (int i = 1; i < cElement.getChildNodes().getLength() + 1; i++) {
					if (cElement.getElementsByTagName("cover" + i).item(0) == null) {
						continue;
					}

					bookCoverPaths.add(((Element) cElement.getElementsByTagName("cover" + i).item(0)).getTextContent());
				}
				
				for(int i = 1; i < gElement.getChildNodes().getLength() + 1; i++){
					if(gElement.getElementsByTagName("genre" + i).item(0) == null){
						continue;
					}
					
					genres[i] = ((Element) gElement.getElementsByTagName("genre" + i).item(0)).getTextContent();
				}

				if (bookCoverPaths.size() > 0) {
					curCover = bookCoverPaths.get(0);
				}
			}
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public void addCover(String value) {
		if(bookCoverPaths.contains(value)){
			System.out.println("This books already contains the cover: " + value);
		}
		
		File xml = new File(xmlFilePath);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;

		try {
			docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(xml);

			doc.getDocumentElement().normalize();

			Node books = doc.getFirstChild();
			Node covers = doc.getElementsByTagName("covers").item(0);

			Element e = doc.createElement("cover" + (bookCoverPaths.size() + 1));
			e.appendChild(doc.createTextNode(value));
			covers.appendChild(e);
			bookCoverPaths.add(value);

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(xmlFilePath));
			transformer.transform(source, result);

		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
		} catch (TransformerException e1) {
			e1.printStackTrace();
		}
	}

	public String getCover(int index) {
		return bookCoverPaths.get(index);
	}

	public String[] getCovers() {
		return bookCoverPaths.toArray(new String[0]);
	}

	public String getCurrentCover() {
		return curCover;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public String getLength() {
		return length;
	}

	public String getReader() {
		return reader;
	}

	public String getXml() {
		return xmlFilePath;
	}
	
	public String getDescription(){
		return description;
	}

	public void setCurrentCover(int index) {
		curCover = bookCoverPaths.get(index);
	}

	public void setCurrentCover(String name) {
		for (int i = 0; i < bookCoverPaths.size(); i++) {
			if (bookCoverPaths.get(i).equals(name)) {
				curCover = bookCoverPaths.get(i);
			}
		}
	}
	
	public void reloadXml(){
		bookCoverPaths.clear();
		readXml();
	}
}
