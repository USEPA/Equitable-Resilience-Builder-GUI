package com.epa.erb.utility;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.epa.erb.App;
import com.epa.erb.ERBContentItem;
import com.epa.erb.ERBItemFinder;
import com.epa.erb.forms.AlternativeFormController;
import com.epa.erb.forms.MainFormController;
import com.epa.erb.goal.Goal;
import com.epa.erb.goal.GoalCategory;
import com.epa.erb.indicators.IndicatorCard;
import com.epa.erb.noteboard.NoteBoardRowController;
import com.epa.erb.project.Project;
import com.epa.erb.wordcloud.WordCloudItem;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class XMLManager {

	private App app;

	public XMLManager(App app) {
		this.app = app;
	}

	private IdAssignments idAssignments = new IdAssignments();
	private ERBItemFinder erbItemFinder = new ERBItemFinder();
	private Logger logger = LogManager.getLogger(XMLManager.class);

	public void writeWordCloudDataXML(File xmlFile, ArrayList<WordCloudItem> wordCloudItems) {
		if (xmlFile != null && wordCloudItems != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("wordCloudItems");
				document.appendChild(rootElement);
				for (WordCloudItem wordCloudItem : wordCloudItems) {
					Element wordCloudElement = document.createElement("wordCloudItem");
					wordCloudElement.setAttribute("word", wordCloudItem.getPhrase());
					wordCloudElement.setAttribute("size", String.valueOf(wordCloudItem.getSize()));
					wordCloudElement.setAttribute("count", String.valueOf(wordCloudItem.getCount()));
					rootElement.appendChild(wordCloudElement);
				}

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);

				StreamResult file = new StreamResult(xmlFile);
				transformer.transform(domSource, file);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot writeWordCloudDataXML. xmlFile or categories is null.");
		}
	}

	public void writeNoteboardDataXML(File xmlFile, ArrayList<NoteBoardRowController> categories) {
		if (xmlFile != null && categories != null) {
			try {
//				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
//				Document document = documentBuilder.newDocument();
//				Element rootElement = document.createElement("categories");
//				document.appendChild(rootElement);
//				for (NoteBoardRowController category : categories) {
//					Element categoryElement = document.createElement("category");
//					categoryElement.setAttribute("name", category.getCategoryName());
//					Element notesElement = document.createElement("notes");
//					for (NoteBoardItemController postItNoteController : category.getListOfPostItNoteControllers()) {
//						Element noteElement = document.createElement("note");
//						noteElement.setAttribute("content", postItNoteController.getPostItNoteText());
//						noteElement.setAttribute("color", postItNoteController.getPostItNoteColor());
//						noteElement.setAttribute("likes", postItNoteController.getNumberLabel().getText());
//						noteElement.setAttribute("position",
//								String.valueOf(postItNoteController.getPostItNoteIndex(category)));
//						notesElement.appendChild(noteElement);
//					}
//					categoryElement.appendChild(notesElement);
//					rootElement.appendChild(categoryElement);
//				}
//
//				TransformerFactory transformerFactory = TransformerFactory.newInstance();
//				Transformer transformer = transformerFactory.newTransformer();
//				DOMSource domSource = new DOMSource(document);
//
//				StreamResult file = new StreamResult(xmlFile);
//				transformer.transform(domSource, file);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot writeNoteboardDataXML. xmlFile or categories is null.");
		}
	}

	public ArrayList<WordCloudItem> parseWordCloudDataXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				NodeList wordCloudItemNodeList = doc.getElementsByTagName("wordCloudItem");
				ArrayList<WordCloudItem> wordCloudItems = new ArrayList<WordCloudItem>();
				for (int i = 0; i < wordCloudItemNodeList.getLength(); i++) {
					Node wordCloudItemNode = wordCloudItemNodeList.item(i);
					// Category
					if (wordCloudItemNode.getNodeType() == Node.ELEMENT_NODE) {
						Element wordCloudItemElement = (Element) wordCloudItemNode;
						String word = wordCloudItemElement.getAttribute("word");
						int size = Integer.parseInt(wordCloudItemElement.getAttribute("size"));
						int count = Integer.parseInt(wordCloudItemElement.getAttribute("count"));

						WordCloudItem wordCloudItem = new WordCloudItem(false, word, null, null, count, size);
						wordCloudItems.add(wordCloudItem);
					}
				}
				return wordCloudItems;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}

	public ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> parseNoteboardDataXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				NodeList categoryNodeList = doc.getElementsByTagName("category");
				ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>> listOfCategoryHashMaps = new ArrayList<HashMap<String, ArrayList<HashMap<String, String>>>>();
				for (int i = 0; i < categoryNodeList.getLength(); i++) {
					Node categoryNode = categoryNodeList.item(i);
					// Category
					if (categoryNode.getNodeType() == Node.ELEMENT_NODE) {
						Element categoryElement = (Element) categoryNode;
						String categoryName = categoryElement.getAttribute("name");
						HashMap<String, ArrayList<HashMap<String, String>>> categoryHashMaps = new HashMap<String, ArrayList<HashMap<String, String>>>();
						NodeList noteNodeList = categoryElement.getElementsByTagName("note");
						ArrayList<HashMap<String, String>> listOfNotesHashMaps = new ArrayList<HashMap<String, String>>();
						for (int j = 0; j < noteNodeList.getLength(); j++) {
							Node noteNode = noteNodeList.item(j);
							// Note
							if (noteNode.getNodeType() == Node.ELEMENT_NODE) {
								HashMap<String, String> noteHashMap = new HashMap<String, String>();
								Element nodeElement = (Element) noteNode;
								String color = nodeElement.getAttribute("color");
								noteHashMap.put("color", color);
								String content = nodeElement.getAttribute("content");
								noteHashMap.put("content", content);
								String likes = nodeElement.getAttribute("likes");
								noteHashMap.put("likes", likes);
								String position = nodeElement.getAttribute("position");
								noteHashMap.put("position", position);
								listOfNotesHashMaps.add(noteHashMap);
							}
						}
						categoryHashMaps.put(categoryName, listOfNotesHashMaps);
						listOfCategoryHashMaps.add(categoryHashMaps);
					}
				}
				return listOfCategoryHashMaps;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}
	
	public ArrayList<String> parseWorksheetsXML(File xmlFile, String sectionName) {
		if (xmlFile != null && xmlFile.exists()) {
			try {
				ArrayList<String> worksheets = new ArrayList<String>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				NodeList sectionNodeList = doc.getElementsByTagName("section");
				for (int i = 0; i < sectionNodeList.getLength(); i++) {
					Node sectionNode = sectionNodeList.item(i);
					if (sectionNode.getNodeType() == Node.ELEMENT_NODE) {
						Element sectionElement = (Element) sectionNode;
						String sectionString = sectionElement.getAttribute("name");
						if (sectionString.contentEquals(sectionName)) {
							NodeList worksheetNodeList = sectionElement.getElementsByTagName("worksheet");
							for (int j = 0; j < worksheetNodeList.getLength(); j++) {
								Node worksheetNode = worksheetNodeList.item(j);
								if (worksheetNode.getNodeType() == Node.ELEMENT_NODE) {
									Element worksheetElement = (Element) worksheetNode;
									String worksheetName = worksheetElement.getAttribute("name");
									worksheets.add(worksheetName);
								}
							}
						}
					}
				}
				return worksheets;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot parseWorksheetsXML. xmlFile = " + xmlFile);
		}
		return null;
	}
	
	public ArrayList<IndicatorCard> parseIndicatorCardsXML(File xmlFile){
		if (xmlFile != null && xmlFile.exists()) {
			try {
				ArrayList<IndicatorCard> indicatorCards = new ArrayList<IndicatorCard>();
//				FileHandler fileHandler = new FileHandler();
//				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//				Document doc = dBuilder.parse(xmlFile);
//				doc.getDocumentElement().normalize();
//
//				NodeList cardNodeList = doc.getElementsByTagName("card");
//				for (int i = 0; i < cardNodeList.getLength(); i++) {
//					Node cardNode = cardNodeList.item(i);
//					if (cardNode.getNodeType() == Node.ELEMENT_NODE) {
//						Element cardElement = (Element) cardNode;
//						String id = cardElement.getAttribute("id");
//						String system = cardElement.getAttribute("system");
//						String indicator = cardElement.getAttribute("indicator");
//						String definition = cardElement.getAttribute("definition");
//						String resilienceValue = cardElement.getAttribute("resilienceValue");
//						String equityValue = cardElement.getAttribute("equityValue");
//						String localConcern = cardElement.getAttribute("localConcern");
//						String quantitativeDataSources = cardElement.getAttribute("quantitativeDataSources");
//						String quantitativeCollProcess = cardElement.getAttribute("quantitativeCollProcess");
//						String qualitativeCollProcess = cardElement.getAttribute("qualitativeCollProcess");
//						String additionalInformation = cardElement.getAttribute("additionalInformation");
//						String rawDataCollectionNotes = cardElement.getAttribute("rawDataCollectionNotes");
//						String dataPoints = cardElement.getAttribute("dataPoints");
//						String thresholds = cardElement.getAttribute("thresholds");
//						
//						ArrayList<String> questionsToAnswer = new ArrayList<String>();
//						NodeList questions = cardElement.getElementsByTagName("questionsToAnswer");
//						Element questionsElement = (Element) questions.item(0);						
//						NodeList q = questionsElement.getElementsByTagName("question");
//						for(int j=0; j<q.getLength(); j++) {
//							Node qNode = q.item(j);
//							if (qNode.getNodeType() == Node.ELEMENT_NODE) {
//								Element qElement = (Element) qNode;
//								String questionText = qElement.getAttribute("text");
//								questionsToAnswer.add(questionText);
//							}
//						}
//						IndicatorCard indicatorCard = new IndicatorCard(id, system, indicator, definition, resilienceValue, equityValue, localConcern, quantitativeDataSources, quantitativeCollProcess, qualitativeCollProcess, additionalInformation, rawDataCollectionNotes, dataPoints, thresholds, questionsToAnswer);
//						indicatorCards.add(indicatorCard);
//					}
//				}
				return indicatorCards;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot parseFormContentXML. xmlFile = " + xmlFile);
		}
		return null;
	}

	public HashMap<String, ArrayList<HBox>> parseMainFormContentXML(File xmlFile, MainFormController formContentController) {
		if (xmlFile != null && xmlFile.exists()) {
			try {
				FileHandler fileHandler = new FileHandler();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				HashMap<String, ArrayList<HBox>> contentHashMap = new HashMap<String, ArrayList<HBox>>();
				NodeList containerNodeList = doc.getElementsByTagName("container");
				for (int i = 0; i < containerNodeList.getLength(); i++) {
					Node containerNode = containerNodeList.item(i);
					if (containerNode.getNodeType() == Node.ELEMENT_NODE) {
						// Container
						Element containerElement = (Element) containerNode;
						String containerId = containerElement.getAttribute("id");
						ArrayList<HBox> textBlocks = new ArrayList<HBox>();
						NodeList textBlockNodeList = containerElement.getElementsByTagName("textBlock");
						for (int j = 0; j < textBlockNodeList.getLength(); j++) {
							Node textBlockNode = textBlockNodeList.item(j);
							// TextBlock
							if (textBlockNode.getNodeType() == Node.ELEMENT_NODE) {
								HBox textFlowHBox = new HBox();
								TextFlow textFlow = new TextFlow();
								Element textBlockElement = (Element) textBlockNode;
								NodeList textBlockChildrenNodeList = textBlockElement.getChildNodes();
								for (int l = 0; l < textBlockChildrenNodeList.getLength(); l++) {
									Node childNode = textBlockChildrenNodeList.item(l);
									if (childNode.getNodeType() == Node.ELEMENT_NODE) {
										String nodeName = childNode.getNodeName();
										if (nodeName.contentEquals("text")) {
											Node textNode = childNode;
											// Text
											Element textElement = (Element) textNode;
											double size = Double.parseDouble(textElement.getAttribute("size"));
											String fontFamily = textElement.getAttribute("fontFamily");
											String fontStyle = textElement.getAttribute("fontStyle");
											String text = textElement.getAttribute("text");
											Text t = new Text(text);
											if (fontStyle.contentEquals("Hyperlink")) {
												String linkType = textElement.getAttribute("linkType");
												String link = textElement.getAttribute("link");
												t.setStyle("-fx-fill:#4d90bc");
												t.setFont(Font.font(fontFamily, FontWeight.NORMAL, size));
												t.setOnMouseEntered(e -> t.setUnderline(true));
												t.setOnMouseExited(e -> t.setUnderline(false));
												t.setOnMouseClicked(e -> formContentController.handleHyperlink(t, linkType, link, app.getSelectedProject()));
											} else {
												if (fontStyle.contentEquals("Bold")) {
													t.setFont(Font.font(fontFamily, FontWeight.BOLD, size));
												} else if (fontStyle.contentEquals("Underlined")) {
													t.setUnderline(true);
													t.setFont(Font.font(fontFamily, size));
												} else if (fontStyle.contentEquals("Italic")) {
													t.setFont(Font.font(fontFamily, FontPosture.ITALIC, size));
												} else {
													t.setFont(Font.font(fontFamily, FontWeight.NORMAL, size));
												}
											}
											textFlow.getChildren().add(t);
										} else if (nodeName.contentEquals("icon")) {
											Node iconNode = childNode;
											// Icon
											Element iconElement = (Element) iconNode;
											double width = Double.parseDouble(iconElement.getAttribute("width"));
											double height = Double.parseDouble(iconElement.getAttribute("height"));
											String id = iconElement.getAttribute("id");
											ImageView imageView = new ImageView();
											imageView.setImage(new Image(fileHandler.getStaticIconImageFile(id).getPath()));
											imageView.setFitWidth(width);
											imageView.setFitHeight(height);
											Tooltip toolTip = new Tooltip("Click image to expand");
											Tooltip.install(imageView, toolTip);
											imageView.setOnMouseClicked(e -> formContentController.handleImageClicked(e, fileHandler.getStaticIconImageFile(id), imageView, id));
											textFlowHBox.getChildren().add(imageView);
										} else if (nodeName.contentEquals("listBlock")) {
											Node listBlockNode = childNode;
											Element listBlockElement = (Element) listBlockNode;
											NodeList childrenNodeList = listBlockElement.getChildNodes();
											VBox listVBox = new VBox();
											listVBox.setMaxWidth(400.00);
											for (int n = 0; n < childrenNodeList.getLength(); n++) {
												Node cNode = childrenNodeList.item(n);
												if (cNode.getNodeType() == Node.ELEMENT_NODE) {
													String nName = cNode.getNodeName();
													if (nName.contentEquals("text")) {
														Node textNode = cNode;
														if (textNode.getNodeType() == Node.ELEMENT_NODE) {
															Element textElement = (Element) textNode;
															double size = Double.parseDouble(textElement.getAttribute("size"));
															String fontFamily = textElement.getAttribute("fontFamily");
															String fontStyle = textElement.getAttribute("fontStyle");
															TextFlow lTextFlow = new TextFlow();
															String text = textElement.getAttribute("text");
															Text t = new Text(text);
															if (fontStyle.contentEquals("Hyperlink")) {
																String linkType = textElement.getAttribute("linkType");
																String link = textElement.getAttribute("link");
																t.setStyle("-fx-fill:#4d90bc");
																t.setFont(Font.font(fontFamily, FontWeight.NORMAL, size));
																t.setOnMouseEntered(e -> t.setUnderline(true));
																t.setOnMouseExited(e -> t.setUnderline(false));
																t.setOnMouseClicked(e -> formContentController.handleHyperlink(t,linkType, link, app.getSelectedProject()));
															} else {
																if (fontStyle.contentEquals("Bold")) {
																	t.setFont(Font.font(fontFamily, FontWeight.BOLD, size));
																} else if (fontStyle.contentEquals("Underlined")) {
																	t.setUnderline(true);
																	t.setFont(Font.font(fontFamily, size));
																} else {
																	t.setFont(Font.font(fontFamily, FontWeight.NORMAL, size));
																}
															}
															lTextFlow.getChildren().add(t);
															listVBox.getChildren().add(lTextFlow);
														}
													} else if (nName.contentEquals("icon")) {
														Node iconNode = cNode;
														// Icon
														Element iconElement = (Element) iconNode;
														double width = Double.parseDouble(iconElement.getAttribute("width"));
														double height = Double.parseDouble(iconElement.getAttribute("height"));
														String id = iconElement.getAttribute("id");
														ImageView imageView = new ImageView();
														imageView.setImage(new Image(fileHandler.getStaticIconImageFile(id).getPath()));
														imageView.setFitWidth(width);
														imageView.setFitHeight(height);
														Tooltip toolTip = new Tooltip("Please click image to view");
														Tooltip.install(imageView, toolTip);
														imageView.setOnMouseClicked(e -> formContentController.handleImageClicked(e, fileHandler.getStaticIconImageFile(id), imageView, id));
														listVBox.getChildren().add(imageView);
													}

												}

											}
											textFlowHBox.getChildren().add(listVBox);
										}
									}
								}
								textFlowHBox.setSpacing(10.0);
								textFlowHBox.getChildren().add(textFlow);
								textFlowHBox.setAlignment(Pos.CENTER_LEFT);
								textBlocks.add(textFlowHBox);
							}
						}
						contentHashMap.put(containerId, textBlocks);
					}
				}
				return contentHashMap;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot parseFormContentXML. xmlFile = " + xmlFile);
		}
		return null;
	}

	public HashMap<String, ArrayList<HBox>> parseAlternativeFormContentXML(File xmlFile,
			AlternativeFormController formContentController) {
		if (xmlFile != null && xmlFile.exists()) {
			try {
				FileHandler fileHandler = new FileHandler();

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				HashMap<String, ArrayList<HBox>> contentHashMap = new HashMap<String, ArrayList<HBox>>();
				NodeList containerNodeList = doc.getElementsByTagName("container");
				for (int i = 0; i < containerNodeList.getLength(); i++) {
					Node containerNode = containerNodeList.item(i);
					if (containerNode.getNodeType() == Node.ELEMENT_NODE) {
						// Container
						Element containerElement = (Element) containerNode;
						String containerId = containerElement.getAttribute("id");
						ArrayList<HBox> textBlocks = new ArrayList<HBox>();
						NodeList textBlockNodeList = containerElement.getElementsByTagName("textBlock");
						for (int j = 0; j < textBlockNodeList.getLength(); j++) {
							Node textBlockNode = textBlockNodeList.item(j);
							// TextBlock
							if (textBlockNode.getNodeType() == Node.ELEMENT_NODE) {
								HBox textFlowHBox = new HBox();
								TextFlow textFlow = new TextFlow();
								Element textBlockElement = (Element) textBlockNode;
								NodeList textBlockChildrenNodeList = textBlockElement.getChildNodes();
								for (int l = 0; l < textBlockChildrenNodeList.getLength(); l++) {
									Node childNode = textBlockChildrenNodeList.item(l);
									if (childNode.getNodeType() == Node.ELEMENT_NODE) {
										String nodeName = childNode.getNodeName();
										if (nodeName.contentEquals("text")) {
											Node textNode = childNode;
											// Text
											Element textElement = (Element) textNode;
											double size = Double.parseDouble(textElement.getAttribute("size"));
											String fontFamily = textElement.getAttribute("fontFamily");
											String fontStyle = textElement.getAttribute("fontStyle");
											String text = textElement.getAttribute("text");
											Text t = new Text(text);
											if (fontStyle.contentEquals("Hyperlink")) {
												String linkType = textElement.getAttribute("linkType");
												String link = textElement.getAttribute("link");
												t.setStyle("-fx-fill:#4d90bc");
												t.setFont(Font.font(fontFamily, FontWeight.NORMAL, size));
												t.setOnMouseEntered(e -> t.setUnderline(true));
												t.setOnMouseExited(e -> t.setUnderline(false));
												t.setOnMouseClicked(e -> formContentController.handleHyperlink(t, linkType, link, app.getSelectedProject()));
											} else {
												if (fontStyle.contentEquals("Bold")) {
													t.setFont(Font.font(fontFamily, FontWeight.BOLD, size));
												} else if (fontStyle.contentEquals("Underlined")) {
													t.setUnderline(true);
													t.setFont(Font.font(fontFamily, size));
												} else {
													t.setFont(Font.font(fontFamily, FontWeight.NORMAL, size));
												}
											}
											textFlow.getChildren().add(t);
										} else if (nodeName.contentEquals("icon")) {
											Node iconNode = childNode;
											// Icon
											Element iconElement = (Element) iconNode;
											double width = Double.parseDouble(iconElement.getAttribute("width"));
											double height = Double.parseDouble(iconElement.getAttribute("height"));
											String id = iconElement.getAttribute("id");
											ImageView imageView = new ImageView();
											imageView.setImage(new Image(fileHandler.getStaticIconImageFile(id).getPath()));
											imageView.setFitWidth(width);
											imageView.setFitHeight(height);
											imageView.setOnMouseClicked(e-> formContentController.handleImageClicked(e, fileHandler.getStaticIconImageFile(id), imageView, id));
											textFlowHBox.getChildren().add(imageView);											
										} else if(nodeName.contentEquals("listBlock")) {
											Node listBlockNode = childNode;
											Element listBlockElement = (Element) listBlockNode;
											NodeList textInListNodeList = listBlockElement.getElementsByTagName("text");
											VBox listVBox = new VBox();
											for(int m=0; m<textInListNodeList.getLength();m++) {
												Node textNode = textInListNodeList.item(m);
												if(textNode.getNodeType() == Node.ELEMENT_NODE) {
													Element textElement = (Element) textNode;
													double size = Double.parseDouble(textElement.getAttribute("size"));
													String fontFamily = textElement.getAttribute("fontFamily");
													String fontStyle = textElement.getAttribute("fontStyle");
													String text = textElement.getAttribute("text");
													TextFlow tflow = new TextFlow();
													Text t = new Text(text);
													if (fontStyle.contentEquals("Hyperlink")) {
														String linkType = textElement.getAttribute("linkType");
														String link = textElement.getAttribute("link");
														t.setStyle("-fx-fill:#4d90bc");
														t.setFont(Font.font(fontFamily, FontWeight.NORMAL, size));
														t.setOnMouseEntered(e -> t.setUnderline(true));
														t.setOnMouseExited(e -> t.setUnderline(false));
														t.setOnMouseClicked(e -> formContentController.handleHyperlink(t, linkType, link, app.getSelectedProject()));
													} else {
														if (fontStyle.contentEquals("Bold")) {
															t.setFont(Font.font(fontFamily, FontWeight.BOLD, size));
														} else if (fontStyle.contentEquals("Underlined")) {
															t.setUnderline(true);
															t.setFont(Font.font(fontFamily, size));
														} else {
															t.setFont(Font.font(fontFamily, FontWeight.NORMAL, size));
														}
													}
													tflow.getChildren().add(t);
													listVBox.getChildren().add(tflow);
												}
											}
											textFlowHBox.getChildren().add(listVBox);
										}
									}
								}
								textFlowHBox.setSpacing(10.0);
								textFlowHBox.getChildren().add(textFlow);											
								textFlowHBox.setAlignment(Pos.CENTER_LEFT);
								textBlocks.add(textFlowHBox);
							}
						}
						contentHashMap.put(containerId, textBlocks);
					}
				}
				return contentHashMap;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot parseFormContentXML. xmlFile = " + xmlFile);
		}
		return null;
	}

	public HashMap<ArrayList<Text>, String> parseOutputFormContentXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				HashMap<ArrayList<Text>, String> contentList = new LinkedHashMap<ArrayList<Text>, String>();

				NodeList containerNodeList = doc.getElementsByTagName("container");
				for (int i = 0; i < containerNodeList.getLength(); i++) {
					Node containerNode = containerNodeList.item(i);
					if (containerNode.getNodeType() == Node.ELEMENT_NODE) {
						// Container
						Element containerElement = (Element) containerNode;
						NodeList textBlockNodeList = containerElement.getElementsByTagName("textBlock");
						for (int j = 0; j < textBlockNodeList.getLength(); j++) {
							Node textBlockNode = textBlockNodeList.item(j);
							// TextBlock
							if (textBlockNode.getNodeType() == Node.ELEMENT_NODE) {
								Element textBlockElement = (Element) textBlockNode;
								String textBlockType = textBlockElement.getAttribute("type");
								ArrayList<Text> textList = new ArrayList<Text>();
								NodeList textNodeList = textBlockElement.getElementsByTagName("text");
								for (int k = 0; k < textNodeList.getLength(); k++) {
									Node textNode = textNodeList.item(k);
									// Text
									if (textNode.getNodeType() == Node.ELEMENT_NODE) {
										Element textElement = (Element) textNode;
										double size = Double.parseDouble(textElement.getAttribute("size"));
										String fontFamily = textElement.getAttribute("fontFamily");
										String fontStyle = textElement.getAttribute("fontStyle");
										String text = textElement.getAttribute("text");

										Text t = new Text(text);
										t.setFont(Font.font(fontFamily, size));
										t.setId(fontStyle);
										textList.add(t);
									}
								}
								contentList.put(textList, textBlockType);
							}
						}
					}
				}
				return contentList;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot parseFormContentXML. xmlFile = " + xmlFile);
		}
		return null;
	}

	public void writeOutputFormDataXML(File xmlFile, ArrayList<javafx.scene.Node> outputFormNodes) {
		if (xmlFile != null && outputFormNodes != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("textBlocks");
				document.appendChild(rootElement);
				Element containerElement = document.createElement("container");
				containerElement.setAttribute("id", "formVBox");
				for (javafx.scene.Node node : outputFormNodes) {
					Element textBlockElement = document.createElement("textBlock");
					textBlockElement.setAttribute("type", node.getId());

					if (node.toString().contains("TextFlow")) {
						TextFlow textFlow = (TextFlow) node;
						for (int i = 0; i < textFlow.getChildren().size(); i++) {
							Text text = (Text) textFlow.getChildren().get(i);
							Element textElement = document.createElement("text");
							textElement.setAttribute("size", String.valueOf(text.getFont().getSize()));
							textElement.setAttribute("fontFamily", text.getFont().getFamily());
							textElement.setAttribute("fontStyle", text.getId());
							textElement.setAttribute("text", text.getText());
							textBlockElement.appendChild(textElement);
						}
					} else if (node.toString().contains("TextArea")) {
						TextArea textArea = (TextArea) node;
						Element textElement = document.createElement("text");
						textElement.setAttribute("size", "16.0");
						textElement.setAttribute("fontFamily", "System");
						if (textArea.getText() != null && textArea.getText().length() > 0) {
							textElement.setAttribute("text", textArea.getText());
							textElement.setAttribute("fontStyle", "Regular");
						} else {
							textElement.setAttribute("text", textArea.getPromptText());
							textElement.setAttribute("fontStyle", "Prompt");
						}
						textBlockElement.appendChild(textElement);
					}
					containerElement.appendChild(textBlockElement);
				}
				rootElement.appendChild(containerElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);

				StreamResult file = new StreamResult(xmlFile);
				transformer.transform(domSource, file);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot writeOutputFormDataXML. xmlFile or outputFormNodes is null.");
		}
	}

	// -------------------------------------CHECKED------------------------------------------------------------------

	public ArrayList<ERBContentItem> parseContentXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				ArrayList<ERBContentItem> availableERBContentItems = new ArrayList<ERBContentItem>();
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList contentNodeList = doc.getElementsByTagName("content");
				for (int i = 0; i < contentNodeList.getLength(); i++) {
					Node contentNode = contentNodeList.item(i);
					if (contentNode.getNodeType() == Node.ELEMENT_NODE) {
						Element contentElement = (Element) contentNode;
						String id = contentElement.getAttribute("id");
						String guid = contentElement.getAttribute("guid");
						String type = contentElement.getAttribute("type");
						String status = contentElement.getAttribute("status");
						String longName = contentElement.getAttribute("longName");
						String shortName = contentElement.getAttribute("shortName");
						ERBContentItem erbContentItem = new ERBContentItem(id, guid, type, status, longName, shortName);
						availableERBContentItems.add(erbContentItem);
					}
				}
				return availableERBContentItems;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return null;
	}

	ERBContentItem goalCategoryERBContentItem;
	ArrayList<ERBContentItem> nestedGoalCategoryERBContentItems = new ArrayList<ERBContentItem>();
	ArrayList<ERBContentItem> allGoalCategoryERBContentItems = new ArrayList<ERBContentItem>();

	// Parse GoalCategories
	public ArrayList<GoalCategory> parseGoalCategoriesXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				ArrayList<GoalCategory> goalCategories = new ArrayList<GoalCategory>();
				NodeList goalCategoryNodeList = doc.getElementsByTagName("goalCategory");
				for (int h = 0; h < goalCategoryNodeList.getLength(); h++) {
					Node goalCategoryNode = goalCategoryNodeList.item(h);
					if (goalCategoryNode.getNodeType() == Node.ELEMENT_NODE) {
						Element goalCategoryElement = (Element) goalCategoryNode;
						String goalCategoryName = goalCategoryElement.getAttribute("categoryName");
						parseGoalCategoryNodes(goalCategoryNode, null);
						GoalCategory goalCategory = new GoalCategory(goalCategoryName, nestedGoalCategoryERBContentItems);
						goalCategories.add(goalCategory);
					}
				}
				return goalCategories;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}

	public void parseGoalCategoryNodes(Node node, Node parent) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element nodeElement = (Element) node;
			String id = nodeElement.getAttribute("id").trim();
			ERBContentItem erbContentItem = app.findERBContentItemForId(id);
			ERBContentItem clonedContentItem = erbContentItem;
			if (erbContentItem != null) clonedContentItem = new ERBContentItem(erbContentItem.getId(), erbContentItem.getGuid(), erbContentItem.getType(), erbContentItem.getStatus(), erbContentItem.getLongName(), erbContentItem.getShortName());
			allGoalCategoryERBContentItems.add(clonedContentItem);
			if (id != null && id.length() > 0) {
				if (idAssignments.getChapterIdAssignments().contains(id)) {
					nestedGoalCategoryERBContentItems.add(clonedContentItem);
				} else {
					Element parentElement = (Element) parent;
					goalCategoryERBContentItem = findParentERBContentItem(parentElement.getAttribute("id"), allGoalCategoryERBContentItems);
					goalCategoryERBContentItem.addChildERBContentItem(clonedContentItem);
				}
			}
			if (node.hasChildNodes()) {
				NodeList children = node.getChildNodes();
				for (int i = 0; i < children.getLength(); i++) {
					parseGoalCategoryNodes(children.item(i), node);
				}
			}
		}
	}

	public ArrayList<Project> parseAllProjects(File ERBProjectDirectory) {
		ArrayList<Project> projects = new ArrayList<Project>();
		if (ERBProjectDirectory != null && ERBProjectDirectory.exists()) {
			File[] projectDirectories = ERBProjectDirectory.listFiles();
			for (File projectDirectory : projectDirectories) {
				if (projectDirectory.isDirectory()) {
					File projectMetaFile = new File(projectDirectory.getPath() + "\\Meta.xml");
					if (projectMetaFile.exists()) {
						Project project = parseProjectXML(projectMetaFile);
						projects.add(project);
					}
				}
			}
		}
		return projects;
	}
	
	public Project parseProjectXML(File xmlFile) {
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList projectNodeList = doc.getElementsByTagName("project");
				for (int i = 0; i < projectNodeList.getLength(); i++) {
					Node projectNode = projectNodeList.item(i);
					// Project
					if (projectNode.getNodeType() == Node.ELEMENT_NODE) {
						Element projectElement = (Element) projectNode;
						String projectName = projectElement.getAttribute("projectName");
						String projectType = projectElement.getAttribute("projectType");
						String projectCleanedName = projectElement.getAttribute("projectCleanedName");
						String projectDescription = projectElement.getAttribute("projectDescription");

						ArrayList<Goal> listOfGoals = new ArrayList<Goal>();
						NodeList goalsNodeList = projectElement.getElementsByTagName("goal");
						for (int j = 0; j < goalsNodeList.getLength(); j++) {
							Node goalNode = goalsNodeList.item(j);
							// Goal
							if (goalNode.getNodeType() == Node.ELEMENT_NODE) {
								Element goalElement = (Element) goalNode;
								String goalName = goalElement.getAttribute("goalName");
								String goalCleanedName = goalElement.getAttribute("goalCleanedName");
								ArrayList<GoalCategory> listOfSelectedGoalCategories = new ArrayList<GoalCategory>();
								String goalDescription = goalElement.getAttribute("goalDescription");
								NodeList goalCategoryNodeList = goalElement.getElementsByTagName("goalCategory");
								for (int k = 0; k < goalCategoryNodeList.getLength(); k++) {
									Node goalCategoryNode = goalCategoryNodeList.item(k);
									// Goal Category
									if (goalCategoryNode.getNodeType() == Node.ELEMENT_NODE) {
										Element goalCategoryElement = (Element) goalCategoryNode;
										String categoryName = goalCategoryElement.getAttribute("categoryName");
										GoalCategory goalCategory = erbItemFinder.getGoalCategoryByName(app.getAvailableGoalCategories(), categoryName);
										listOfSelectedGoalCategories.add(goalCategory);
									}
								}
								Goal goal = new Goal(app, goalName, goalCleanedName, goalDescription, listOfSelectedGoalCategories);
								listOfGoals.add(goal);
							}
						}
						Project project = new Project(projectName, projectType, projectCleanedName, projectDescription, listOfGoals);
						for (Goal goal : listOfGoals) {
							goal.setContentItems(project);
						}
						return project;
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}

	public void writeProjectMetaXML(File xmlFile, Project project) {
		if (xmlFile != null && project != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("project");
				document.appendChild(rootElement);
				rootElement.setAttribute("projectName", project.getProjectName());
				rootElement.setAttribute("projectType", project.getProjectType());
				rootElement.setAttribute("projectCleanedName", project.getProjectCleanedName());
				rootElement.setAttribute("projectDescription", project.getProjectDescription());
				Element goalsElement = document.createElement("goals");
				for (Goal goal : project.getProjectGoals()) {
					Element goalElement = document.createElement("goal");
					goalElement.setAttribute("goalName", goal.getGoalName());
					goalElement.setAttribute("goalCleanedName", goal.getGoalCleanedName());
					goalElement.setAttribute("goalDescription", goal.getGoalDescription());
					Element goalsCategoryElement = document.createElement("goalsCategories");
					for (GoalCategory goalCategory : goal.getListOfSelectedGoalCategories()) {
						Element goalCategoryElement = document.createElement("goalCategory");
						goalCategoryElement.setAttribute("categoryName", goalCategory.getCategoryName());
						goalsCategoryElement.appendChild(goalCategoryElement);
					}
					goalElement.appendChild(goalsCategoryElement);
					goalsElement.appendChild(goalElement);
				}
				rootElement.appendChild(goalsElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);
				StreamResult file = new StreamResult(xmlFile);
				transformer.transform(domSource, file);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot writeProjectMetaXML. xmlFile or project is null.");
		}
	}

	public void writeGoalMetaXML(File xmlFile, ArrayList<ERBContentItem> erbContentItems) {
		if (xmlFile != null && erbContentItems != null) {
			try {
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = documentBuilder.newDocument();
				Element rootElement = document.createElement("contents");
				document.appendChild(rootElement);
				for (ERBContentItem erbContentItem : erbContentItems) {
					writeGoalNodes(erbContentItem, null, rootElement, document);
				}
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource domSource = new DOMSource(document);
				StreamResult file = new StreamResult(xmlFile);
				transformer.transform(domSource, file);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Cannot writeGoalMetaXML. xmlFile or erbContentItems is null.");
		}
	}

	Element elementToTrack;
	ArrayList<Element> listOfElements = new ArrayList<Element>();

	public void writeGoalNodes(ERBContentItem erbContentItem, ERBContentItem erbContentItemParent, Element rootElement, Document document) {
		Element element = document.createElement("content");
		element.setAttribute("id", erbContentItem.getId());
		element.setAttribute("guid", erbContentItem.getGuid());
		element.setAttribute("type", erbContentItem.getType());
		element.setAttribute("status", erbContentItem.getStatus());
		element.setAttribute("longName", erbContentItem.getLongName());
		element.setAttribute("shortName", erbContentItem.getShortName());
		listOfElements.add(element);
		if (idAssignments.getChapterIdAssignments().contains(erbContentItem.getId())) {
			rootElement.appendChild(element);
			elementToTrack = element;
		} else {
			Element parentElement = searchForGoalElementById(erbContentItemParent.getId());
			if (parentElement != null) parentElement.appendChild(element);
		}
		if (erbContentItem.getChildERBContentItems().size() > 0) {
			for (ERBContentItem erbContentItemChild : erbContentItem.getChildERBContentItems()) {
				writeGoalNodes(erbContentItemChild, erbContentItem, rootElement, document);
			}
		}
	}

	private Element searchForGoalElementById(String id) {
		for (Element e : listOfElements) {
			if (e.getAttribute("id").contentEquals(id)) {
				return e;
			}
		}
		return null;
	}
	
	ERBContentItem goalERBContentItem = new ERBContentItem();
	ArrayList<ERBContentItem> nestedGoalERBContentItems = new ArrayList<ERBContentItem>();
	ArrayList<ERBContentItem> allGoalERBContentItems = new ArrayList<ERBContentItem>();

	public ArrayList<ERBContentItem> parseGoalXML(File xmlFile) {
		allGoalERBContentItems.clear();
		if (xmlFile != null && xmlFile.exists() && xmlFile.canRead()) {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				NodeList contentsList = doc.getElementsByTagName("contents");
				for (int h = 0; h < contentsList.getLength(); h++) {
					Node contentsNode = contentsList.item(h);
					if (contentsNode.getNodeType() == Node.ELEMENT_NODE) {
						parseGoalNodes(contentsNode, null);
					}
				}
				return nestedGoalERBContentItems;
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error(xmlFile.getPath() + " either does not exist or cannot be read");
		}
		return null;
	}

	public void parseGoalNodes(Node node, Node parent) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			Element nodeElement = (Element) node;
			String id = nodeElement.getAttribute("id").trim();
			if (id != null && id.length() > 0) {
				String guid = nodeElement.getAttribute("guid");
				String longName = nodeElement.getAttribute("longName");
				String shortName = nodeElement.getAttribute("shortName");
				String status = nodeElement.getAttribute("status");
				String type = nodeElement.getAttribute("type");
				ERBContentItem erbContentItem = new ERBContentItem(id, guid, type, status, longName, shortName);
				allGoalERBContentItems.add(erbContentItem);

				if (idAssignments.getChapterIdAssignments().contains(id)) {
					nestedGoalERBContentItems.add(erbContentItem); // Adding chapter
				} else {
					Element parentElement = (Element) parent;
					String pid = parentElement.getAttribute("id").trim();
					goalERBContentItem = findParentERBContentItem(pid, allGoalERBContentItems);
					if (goalERBContentItem != null) goalERBContentItem.addChildERBContentItem(erbContentItem);
				}
			}
			if (node.hasChildNodes()) {
				NodeList children = node.getChildNodes();
				for (int i = 0; i < children.getLength(); i++) {
					Node childNode = children.item(i);
					if (childNode.getNodeType() == Node.ELEMENT_NODE) {
						parseGoalNodes(childNode, node);
					}
				}
			}
		}
	}

	private ERBContentItem findParentERBContentItem(String erbContentId, ArrayList<ERBContentItem> itemsToSearch) {
		if (erbContentId != null) {
			for (ERBContentItem erbContentItem : itemsToSearch) {
				if (erbContentItem != null) {
					if (erbContentItem.getId().contentEquals(erbContentId)) {
						return erbContentItem;
					}
				}
			}
		}
		return null;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}
}
