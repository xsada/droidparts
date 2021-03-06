/**
 * Copyright 2014 Alex Yanchenko
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package org.droidparts.test.testcase.serialize;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.droidparts.persist.serializer.XMLSerializer;
import org.droidparts.test.R;
import org.droidparts.test.model.Album;
import org.droidparts.test.model.Album2;
import org.droidparts.test.model.AlbumFail;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.test.AndroidTestCase;

public class XMLTestCase extends AndroidTestCase {

	public void testAlbums() throws Exception {
		Document albumsDoc = getXMLDocument(R.raw.albums_xml);
		NodeList nl = albumsDoc.getElementsByTagName("album");
		XMLSerializer<Album> serializer = new XMLSerializer<Album>(Album.class,
				getContext());
		ArrayList<Album> albums = serializer.deserializeAll(nl);
		assertEquals(2, albums.size());
		assertEquals("Diamond", albums.get(0).name);
		assertEquals(2009, albums.get(1).year);
	}

	public void testAlbum2() throws Exception {
		Document albumDoc = getXMLDocument(R.raw.album2);
		XMLSerializer<Album2> serializer = new XMLSerializer<Album2>(
				Album2.class, getContext());
		Album2 a2 = serializer.deserialize(albumDoc);
		assertEquals("Iris", a2.name);
		assertEquals(2009, a2.year);
		assertEquals(2, a2.albumArr.length);
		assertEquals(2, a2.albumList.size());
		assertEquals(3, a2.ints.length);
		assertEquals(3, a2.integers.size());
		assertEquals(5, a2.ints[1]);
		assertEquals(100500, (int) a2.integers.get(2));
		assertEquals(a2.integers.size(), a2.integersHinted.size());
		assertTrue(a2.integersHintedWrong.isEmpty());
	}

	public void testFail() throws Exception {
		Document albumDoc = getXMLDocument(R.raw.album2);
		XMLSerializer<AlbumFail> serializer = new XMLSerializer<AlbumFail>(
				AlbumFail.class, getContext());
		try {
			serializer.deserialize(albumDoc);
			assertTrue(false);
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

	private Document getXMLDocument(int resId) throws Exception {
		DocumentBuilder db = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = db.parse(new InputSource(getContext().getResources()
				.openRawResource(resId)));
		return doc;
	}

}
