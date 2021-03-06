// DataMatrixTest.java
//------------------------------------------------------------------------------
// $Revision: 2360 $
// $Date: 2007-09-04 18:02:00 -0400 (Tue, 04 Sep 2007) $
// $Author: dtenenba $
//--------------------------------------------------------------------------------------
/*
 * Copyright (C) 2006 by Institute for Systems Biology,
 * Seattle, Washington, USA.  All rights reserved.
 *
 * This source code is distributed under the GNU Lesser
 * General Public License, the text of which is available at:
 *   http://www.gnu.org/copyleft/lesser.html
 */

package org.systemsbiology.gaggle.experiment.datamatrix.unitTests;
//--------------------------------------------------------------------------------------
import junit.framework.*;

import java.util.*;

import org.systemsbiology.gaggle.experiment.readers.*;

//------------------------------------------------------------------------------
public class DataMatrixTest extends TestCase {

//------------------------------------------------------------------------------
public DataMatrixTest (String name) 
{
  super (name);
}

//------------------------------------------------------------------------------
public void setUp () throws Exception
{
}
//------------------------------------------------------------------------------
public void tearDown () throws Exception
{
}
//------------------------------------------------------------------------------
public void testSimple4x3Matrix () throws Exception
{
  System.out.println ("testSimple4x3Matrix");

  DataMatrixReader reader = new DataMatrixFileReader ("file://" + "simpleMatrix.txt");
  reader.read ();
  org.systemsbiology.gaggle.core.datatypes.DataMatrix matrix = reader.get ();

  // System.out.println ("column count: " + matrix.getColumnCount ());
  assertTrue (matrix.getColumnCount () == 3);
  assertTrue (matrix.getRowCount () == 4);

  String rowTitlesTitle = matrix.getRowTitlesTitle ();
  String [] columnTitles = matrix.getColumnTitles ();
  String [] rowTitles = matrix.getRowTitles ();

  assertTrue (columnTitles.length == 3);
  assertTrue (rowTitles.length == 4);
  assertTrue (rowTitlesTitle.equals ("gene"));
  String [] expectedRowTitles = {"a", "b", "c", "d"};
  String [] expectedColumnTitles = {"cond0", "cond1", "cond2"};

  for (int i=0; i < columnTitles.length; i++) {
    assertTrue (columnTitles [i].equals (expectedColumnTitles [i]));
    }

  for (int i=0; i < rowTitles.length; i++)
    assertTrue (rowTitles [i].equals (expectedRowTitles [i]));

  double [] row3 = matrix.get (3);
  assertTrue (row3.length == 3);
  assertTrue (row3 [0] == 999.0);
  assertTrue (row3 [1] == 99.0);
  assertTrue (row3 [2] == 9.0);

  double [] row3ByName = matrix.get ("d");
  assertTrue (row3ByName.length == 3);
  assertTrue (row3ByName [0] == 999.0);
  assertTrue (row3ByName [1] == 99.0);
  assertTrue (row3ByName [2] == 9.0);

  assertTrue (matrix.getColumnNumber ("cond0") == 0);
  assertTrue (matrix.getColumnNumber ("cond1") == 1);
  assertTrue (matrix.getColumnNumber ("cond2") == 2);

  try {
    int c = matrix.getColumnNumber ("flapadoodle!");
    assertFalse (true);  // should not execute
    }
  catch (IllegalArgumentException iae) {
    assertTrue (true);
    }

  double [] column2 = matrix.getColumn ("cond1");
  assertTrue (column2.length == 4);
  //for (int i=0; i < 4; i++)
   // System.out.println (i + ") " + column2 [i]);

  assertTrue (column2 [0] == 13.8);
  assertTrue (column2 [1] == -8.0);
  assertTrue (column2 [2] == 0.0);
  assertTrue (column2 [3] == 99.0);

}  // testSimple4x3Matrix
//-------------------------------------------------------------------------
public void testSimple4x3MatrixToString () throws Exception
{
  System.out.println ("testSimple4x3MatrixToString");

  DataMatrixReader reader = new DataMatrixFileReader ("file://" + "simpleMatrix.txt");
  reader.read ();
  org.systemsbiology.gaggle.core.datatypes.DataMatrix matrix = reader.get ();
  String actualMatrixAsString = matrix.toString ();
  String expected = "gene	cond0	cond1	cond2\n" + 
                    "a	12.2	13.8	4.0\n" +
                    "b	-1.2	-8.0	-32.3333\n" +
                    "c	0.0	0.0	0.0\n" +
                    "d	999.0	99.0	9.0\n";
  // System.out.println ("actual\n" + actualMatrixAsString);

  assertTrue (expected.equals (actualMatrixAsString));

}  // testSimple4x3Matrix
//-------------------------------------------------------------------------
public void testSetGetSpecies () throws Exception
{
  System.out.println ("testSetGetSpeciesg");

  DataMatrixReader reader = new DataMatrixFileReader ("file://" + "simpleMatrix.txt");
  reader.read ();
  org.systemsbiology.gaggle.core.datatypes.DataMatrix matrix = reader.get ();
  assertTrue (matrix.getSpecies().equals ("unknown"));
  String speciesName = "Halobacterium sp.";
  matrix.setSpecies (speciesName);
  assertTrue (matrix.getSpecies().equals (speciesName));

}  // testSetGetSpecies
//-------------------------------------------------------------------------
/**
 * call matrix.set (row, column, value)
 */
public void testCreateExplicitlyWithCellSet () throws Exception
{
  System.out.println ("testCreateExplicitlyWithCellSet");

  org.systemsbiology.gaggle.core.datatypes.DataMatrix matrix = new org.systemsbiology.gaggle.core.datatypes.DataMatrix();
  int dataRows = 3;
  int dataColumns = 4;
  String [] columnTitles = {"one", "two", "three", "four"};
  String [] rowTitles = {"a", "b", "c"};
  matrix.setSize (3, 4);
  matrix.setRowTitlesTitle ("GENE");
  matrix.setColumnTitles (columnTitles);
  matrix.setRowTitles (rowTitles);

  for (int r=0; r < dataRows; r ++)
    for (int c=0; c < dataColumns; c++)
      matrix.set (r, c, (r * 10.0) + c * 10.0);


  assertTrue (matrix.getColumnCount () == dataColumns);
  assertTrue (matrix.getRowCount () == dataRows);

  assertTrue (Arrays.equals (matrix.getColumnTitles (), columnTitles));
  assertTrue (Arrays.equals (matrix.getRowTitles (), rowTitles));


}  // testCreateExplicitlyWithCellSet
//-------------------------------------------------------------------------
/**
 * call matrix.set (row, values)
 */
public void testCreateExplicitlyWithRowSet () throws Exception
{
  System.out.println ("testCreateExplicitlyWithRowSet");

  org.systemsbiology.gaggle.core.datatypes.DataMatrix matrix = new org.systemsbiology.gaggle.core.datatypes.DataMatrix();
  int dataRows = 3;
  int dataColumns = 4;
  String [] columnTitles = {"one", "two", "three", "four"};
  String [] rowTitles = {"a", "b", "c"};
  matrix.setSize (3, 4);
  matrix.setRowTitlesTitle ("GENE");
  matrix.setColumnTitles (columnTitles);
  matrix.setRowTitles (rowTitles);

  for (int r=0; r < dataRows; r ++) {
    double [] newRow = new double [dataColumns];
    for (int c=0; c < dataColumns; c++)
      newRow [c] = (r * 10.0) + c * 10.0;
    matrix.set (r, newRow);
    } // for r


  assertTrue (matrix.getColumnCount () == dataColumns);
  assertTrue (matrix.getRowCount () == dataRows);

  assertTrue (Arrays.equals (matrix.getColumnTitles (), columnTitles));
  assertTrue (Arrays.equals (matrix.getRowTitles (), rowTitles));


}  // testCreateExplicitlyWithRowSet
//-------------------------------------------------------------------------
public void testGetNames () throws Exception
{
  System.out.println ("testGetNames");

  String filename = "matrix.icat";
  DataMatrixReader reader = new DataMatrixFileReader ("file://" + filename);
  reader.read ();
  org.systemsbiology.gaggle.core.datatypes.DataMatrix matrix = reader.get ();
  String fullName = matrix.getFullName ();
  String shortName = matrix.getShortName ();
  String expected = "file://matrix.icat";
  assertTrue (fullName.equals (expected));
  assertTrue (shortName.equals ("matrix.icat"));

}  // testGetNames
//-------------------------------------------------------------------------
public void testSetName () throws Exception
{
  System.out.println ("testSetNames");

  String filename = "matrix.icat";
  DataMatrixReader reader = new DataMatrixFileReader ("file://" + filename);
  reader.read ();
  org.systemsbiology.gaggle.core.datatypes.DataMatrix matrix = reader.get ();
  String fullName = matrix.getFullName ();
  String shortName = matrix.getShortName ();
  String expected = "file://matrix.icat";
  assertTrue (fullName.equals (expected));
  assertTrue (shortName.equals ("matrix.icat"));

  String newName = "abracadabra"; 
  matrix.setFullName (newName);
  assertTrue (matrix.getFullName().equals (newName));
  assertTrue (matrix.getShortName().equals (newName));

}  // testGetNames
//-------------------------------------------------------------------------
public void testNames () throws Exception
{
  System.out.println ("testNames");

  String filename = "file://matrix.icat";
  org.systemsbiology.gaggle.core.datatypes.DataMatrix dm = new org.systemsbiology.gaggle.core.datatypes.DataMatrix(filename);
  assertTrue (dm.getFileExtension().equals ("icat"));
  assertTrue (dm.getFullName().equals(filename));
  assertTrue (dm.getShortName().equals("matrix.icat"));
  assertTrue (dm.getDataTypeBriefName().equals ("icat"));

  dm = new org.systemsbiology.gaggle.core.datatypes.DataMatrix("http://bogus");
  assertTrue (dm.getFileExtension().equals (""));
  assertTrue (dm.getFullName().equals("http://bogus"));
  assertTrue (dm.getShortName().equals("bogus"));
  assertTrue (dm.getDataTypeBriefName().equals (""));
  dm.setDataTypeBriefName ("mixedBagOfStuff");
  assertTrue (dm.getDataTypeBriefName().equals ("mixedBagOfStuff"));

  dm = new org.systemsbiology.gaggle.core.datatypes.DataMatrix("http://bogus.hip.hop-hap");
  assertTrue (dm.getFileExtension().equals ("hop-hap"));
  assertTrue (dm.getDataTypeBriefName().equals ("hop-hap"));


}  // testFileExtension
//-------------------------------------------------------------------------
public void testAddRow () throws Exception
{
  System.out.println ("testAddRow");

  DataMatrixReader reader = new DataMatrixFileReader ("file://" + "simpleMatrix.txt");
  reader.read ();
  org.systemsbiology.gaggle.core.datatypes.DataMatrix matrix = reader.get ();
  assertTrue (Arrays.equals (matrix.getColumnTitles (), new String [] {"cond0", "cond1", "cond2"}));
  assertTrue (Arrays.equals (matrix.getRowTitles (), new String [] {"a", "b", "c", "d"}));
  assertTrue (Arrays.equals (matrix.get (0), new double [] {12.2, 13.8, 4.0}));
  assertTrue (Arrays.equals (matrix.get (1), new double [] {-1.2, -8.0, -32.3333}));
  assertTrue (Arrays.equals (matrix.get (2), new double [] {0.0, 0.0, 0.0}));
  assertTrue (Arrays.equals (matrix.get (3), new double [] {999.0, 99.0, 9.0}));

  matrix.addRow ("new row", new double [] {1.0, 2.0, 3.0});
  assertTrue (matrix.getRowCount () == 5);

  assertTrue (Arrays.equals (matrix.getRowTitles (), new String [] {"a", "b", "c", "d", "new row"}));
  assertTrue (Arrays.equals (matrix.get (0), new double [] {12.2, 13.8, 4.0}));
  assertTrue (Arrays.equals (matrix.get (1), new double [] {-1.2, -8.0, -32.3333}));
  assertTrue (Arrays.equals (matrix.get (2), new double [] {0.0, 0.0, 0.0}));
  assertTrue (Arrays.equals (matrix.get (3), new double [] {999.0, 99.0, 9.0}));

   // now ensure that adding a  wrong-sized row throws an exception

  try {
    matrix.addRow ("illegal big row", new double [] {1.0, 2.0, 3.0, 4.0, 5.0});
    assertTrue (false);
    }
  catch (IllegalArgumentException e) {
    assertTrue (true);
    }
  

}  // testAddRow
//-------------------------------------------------------------------------
public void testSerialization () throws Exception
{
  System.out.println ("testSerialization");

  DataMatrixReader reader = new DataMatrixFileReader ("file://" + "simpleMatrix.txt");
  reader.read ();
  org.systemsbiology.gaggle.core.datatypes.DataMatrix matrix = reader.get ();
  assertTrue (Arrays.equals (matrix.getColumnTitles (), new String [] {"cond0", "cond1", "cond2"}));
  assertTrue (Arrays.equals (matrix.getRowTitles (), new String [] {"a", "b", "c", "d"}));
  assertTrue (Arrays.equals (matrix.get (0), new double [] {12.2, 13.8, 4.0}));
  assertTrue (Arrays.equals (matrix.get (1), new double [] {-1.2, -8.0, -32.3333}));
  assertTrue (Arrays.equals (matrix.get (2), new double [] {0.0, 0.0, 0.0}));
  assertTrue (Arrays.equals (matrix.get (3), new double [] {999.0, 99.0, 9.0}));
  matrix.writeObject ("matrix.obj");
  org.systemsbiology.gaggle.core.datatypes.DataMatrix reconstituted = org.systemsbiology.gaggle.core.datatypes.DataMatrix.readObject ("matrix.obj");
  assertTrue (reconstituted.equals (matrix));

} // testSerialization
//-------------------------------------------------------------------------
public static void main (String [] args) 
{
  junit.textui.TestRunner.run (new TestSuite (DataMatrixTest.class));

}// main
//------------------------------------------------------------------------------
} // DataMatrixTest
