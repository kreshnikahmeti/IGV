/*
 * Copyright (c) 2007-2011 by The Broad Institute of MIT and Harvard.  All Rights Reserved.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 *
 * THE SOFTWARE IS PROVIDED "AS IS." THE BROAD AND MIT MAKE NO REPRESENTATIONS OR
 * WARRANTES OF ANY KIND CONCERNING THE SOFTWARE, EXPRESS OR IMPLIED, INCLUDING,
 * WITHOUT LIMITATION, WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, NONINFRINGEMENT, OR THE ABSENCE OF LATENT OR OTHER DEFECTS, WHETHER
 * OR NOT DISCOVERABLE.  IN NO EVENT SHALL THE BROAD OR MIT, OR THEIR RESPECTIVE
 * TRUSTEES, DIRECTORS, OFFICERS, EMPLOYEES, AND AFFILIATES BE LIABLE FOR ANY DAMAGES
 * OF ANY KIND, INCLUDING, WITHOUT LIMITATION, INCIDENTAL OR CONSEQUENTIAL DAMAGES,
 * ECONOMIC DAMAGES OR INJURY TO PROPERTY AND LOST PROFITS, REGARDLESS OF WHETHER
 * THE BROAD OR MIT SHALL BE ADVISED, SHALL HAVE OTHER REASON TO KNOW, OR IN FACT
 * SHALL KNOW OF THE POSSIBILITY OF THE FOREGOING.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.broad.igv.sam;

import net.sf.samtools.util.CloseableIterator;
import org.broad.igv.sam.reader.AlignmentQueryReader;
import org.broad.igv.sam.reader.AlignmentReaderFactory;
import org.broad.igv.sam.reader.BAMRemoteQueryReader;
import org.broad.igv.util.ResourceLocator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

/**
 * @author jrobinso
 */
public class BAMRemoteQueryReaderTest {

    public BAMRemoteQueryReaderTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of query method, of class BAMRemoteQueryReader.
     */
    @Test
    public void testQuery() throws IOException {
        /*
        http://www.broadinstitute.org/webservices/igv/bam?method=query&samFile=/broad/1KG/DCC_merged/freeze5/NA12878.pilot2.SLX.bam&chr=16&start=50542554&end=50542722
        String path = "/Users/jrobinso/IGV/Alignments/303KY.8.paired.bam";
        String serverURL = "http://localhost:8080/webservices/igv";
        String chr = "chr7";
        int start = 2244149;
        int end = 2250144;
         * */

        String path = "http://www.broadinstitute.org/igvdata/1KG/DCC_merged/freeze5/NA12878.pilot2.SLX.bam";
        String serverURL = "http://iwww.broadinstitute.org/webservices/igv";
//chr1:98,751,004-98,751,046
        String chr = "1";
        int start = 50542554; //557000;  //
        int end = 50542722; //558000; //
        boolean contained = false;

        Alignment al = null;


        // ResourceLocator rl = new ResourceLocator(serverURL, path);
        ResourceLocator rl = new ResourceLocator(path);

        AlignmentQueryReader samReader = AlignmentReaderFactory.getReader(rl);
        CloseableIterator<Alignment> result2 = samReader.query(chr, start, end, contained);
        long t1 = System.currentTimeMillis();
        int count1 = 0;
        while (result2.hasNext()) {
            al = result2.next();
            al.getAlignmentStart();
            if (count1 == 0) {
                System.out.println(al.getAlignmentStart() + " -> " + al.getEnd() + " " + al.getReadName());
            }
            count1++;
        }
        //System.out.println(al.getAlignmentStart() + " -> " + al.getEnd());
        //System.out.println("Read " + count1 + " records in " + (System.currentTimeMillis() - t1) + " ms");


        BAMRemoteQueryReader instance = new BAMRemoteQueryReader(rl);
        CloseableIterator<Alignment> result = instance.query(chr, start, end, contained);
        long t0 = System.currentTimeMillis();
        int count = 0;
        int lastStart = -1;
        while (result.hasNext()) {
            al = result.next();
            int s = al.getAlignmentStart();
            if (s < lastStart) {
                System.out.println("Sort problem: " + s);
            }
            lastStart = s;
            //if (count == 0) {
            System.out.println(al.getAlignmentStart() + " -> " + al.getEnd() + " " + al.getReadName());
            //}
            count++;
        }
        //System.out.println(al.getAlignmentStart() + " -> " + al.getEnd());
        System.out.println("Read " + count + " records in " + (System.currentTimeMillis() - t0) + " ms");


    }
}