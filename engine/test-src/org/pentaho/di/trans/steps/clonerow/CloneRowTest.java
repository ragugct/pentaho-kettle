package org.pentaho.di.trans.steps.clonerow;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.pentaho.di.core.RowSet;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LoggingObjectInterface;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.steps.mock.StepMockHelper;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

/**
 * @author Andrey Khayrutdinov
 */
public class CloneRowTest {

  private StepMockHelper<CloneRowMeta, CloneRowData> stepMockHelper;

  @Before
  public void setup() {
    stepMockHelper =
      new StepMockHelper<CloneRowMeta, CloneRowData>( "Test CloneRow", CloneRowMeta.class, CloneRowData.class );
    when( stepMockHelper.logChannelInterfaceFactory.create( any(), any( LoggingObjectInterface.class ) ) )
      .thenReturn( stepMockHelper.logChannelInterface );
    when( stepMockHelper.trans.isRunning() ).thenReturn( true );
  }

  @After
  public void tearDown() {
    stepMockHelper.cleanUp();
  }

  @Test( expected = KettleException.class )
  public void nullNrCloneField() throws Exception {
    CloneRow step =
      new CloneRow( stepMockHelper.stepMeta, stepMockHelper.stepDataInterface, 0, stepMockHelper.transMeta,
        stepMockHelper.trans );
    step.init( stepMockHelper.initStepMetaInterface, stepMockHelper.initStepDataInterface );

    RowMetaInterface inputRowMeta = mock( RowMetaInterface.class );
    when( inputRowMeta.getInteger( any( Object[].class ), anyInt() ) ).thenReturn( null );

    RowSet inputRowSet = stepMockHelper.getMockInputRowSet( new Integer[] { null } );
    when( inputRowSet.getRowMeta() ).thenReturn( inputRowMeta );
    step.setInputRowSets( singletonList( inputRowSet ) );

    when( stepMockHelper.processRowsStepMetaInterface.isNrCloneInField() ).thenReturn( true );
    when( stepMockHelper.processRowsStepMetaInterface.getNrCloneField() ).thenReturn( "field" );

    step.processRow( stepMockHelper.processRowsStepMetaInterface, stepMockHelper.processRowsStepDataInterface );
  }
}
