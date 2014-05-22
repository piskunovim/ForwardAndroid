/*public void GetXMLSettings(String xmlstring, TDataBase dbHelper){

    // создаем объект для данных
    ContentValues cv = new ContentValues();


    // подключаемся к БД
    SQLiteDatabase db = dbHelper.getWritableDatabase();
    try {
        Log.d(LOG_TAG, xmlstring); //в xmlstring находится содержимое xml
        XmlPullParser xpp = prepareXpp(xmlstring);


        while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
            switch (xpp.getEventType()) {
                // начало документа
                case XmlPullParser.START_DOCUMENT:
                    Log.d(LOG_TAG, "START_DOCUMENT");
                    break;
                // начало тэга
                case XmlPullParser.START_TAG:
                    Log.d(LOG_TAG, "START_TAG: name = " + xpp.getName()
                            + ", depth = " + xpp.getDepth() + ", attrCount = "
                            + xpp.getAttributeCount());

					String tag_name = xpp.getName();
                    Log.d("Tag got",tag_name);
                    String id = "";

                    if (tag_name.equals("p")){  //если встретили <p...>
                    for (int i = 0; i < xpp.getAttributeCount(); i++) { //прошлись по его аттрибутам
                        if (xpp.getAttributeName(i).equals("i"))
                        {
                           id = xpp.getAttributeValue(i); //запомним идентификатор оператора
                           cv.put("id", xpp.getAttributeValue(i));
                        }
                        else if (xpp.getAttributeName(i).equals("n"))
                        {
                           cv.put("name", xpp.getAttributeValue(i));
                        }
                        else if (xpp.getAttributeName(i).equals("min"))
                        {
                           cv.put("min", xpp.getAttributeValue(i));
                        }
                        else if (xpp.getAttributeName(i).equals("max"))
						{
                           cv.put("max", xpp.getAttributeValue(i));
						   long rowID = db.insert("p", null, cv);
                        }
                    }
                    }
                    else if (tag_name.equals("f")){ //если встретили <f...>
                    for (int i = 0; i < xpp.getAttributeCount(); i++){ //прошлись по его аттрибутам
                        cv.put("id", id);//запишем в поле id идентификатор оператора
                        if (xpp.getAttributeName(i).equals("n"))
                        {
                         cv.put("fn", xpp.getAttributeValue(i));
                        }
                        else if (xpp.getAttributeName(i).equals("c"))
                        {
                            cv.put("fc", xpp.getAttributeValue(i));
                        }
                        else if (xpp.getAttributeName(i).equals("p"))
                        {
                            cv.put("fp", xpp.getAttributeValue(i));
                        }
                        else if (xpp.getAttributeName(i).equals("m"))
                        {
                            cv.put("fm", xpp.getAttributeValue(i));
                        }
                        else if (xpp.getAttributeName(i).equals("r"))
                        {
                            cv.put("fr", xpp.getAttributeValue(i));
                        }
                        else if (xpp.getAttributeName(i).equals("t"))
                        {
                            cv.put("ft", xpp.getAttributeValue(i));
                            long rowID = db.insert("f", null, cv);
                        }
                    }
                    }


                    break;
                // конец тэга
                case XmlPullParser.END_TAG:
                    Log.d(LOG_TAG, "END_TAG: name = " + xpp.getName());
                    break;
                // содержимое тэга
                case XmlPullParser.TEXT:
                    Log.d(LOG_TAG, "text = " + xpp.getText());
                    break;

                default:
                    break;
            }
            // следующий элемент
            xpp.next();
        }
        Log.d(LOG_TAG, "END_DOCUMENT");

    } catch (XmlPullParserException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
   }

    XmlPullParser prepareXpp(String xmlstring) throws XmlPullParserException {
        // получаем фабрику
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // включаем поддержку namespace (по умолчанию выключена)
        factory.setNamespaceAware(true);
        // создаем парсер
        XmlPullParser xpp = factory.newPullParser();
        // даем парсеру на вход Reader
        xpp.setInput(new StringReader(xmlstring));
        return xpp;
    }*/