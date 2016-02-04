package myudfs;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.schema.Schema;
import org.joda.time.DateTime;

public class validate extends EvalFunc<Tuple> {
	
	// Pig Types and Native Java Types
	/*
	Pig Type	Java Class
	bytearray	DataByteArray
	chararray	String
	int			Integer
	long		Long
	float		Float
	double		Double
	boolean		Boolean
	datetime	DateTime
	bigdecimal	BigDecimal
	biginteger	BigInteger
	tuple		Tuple
	bag			DataBag
	map			Map<Object, Object>
	*/

	public Tuple exec(Tuple input) throws IOException {
		String msg = "failed::" +"\n";

		try { 
  
            Tuple output = TupleFactory.getInstance().newTuple(input.size()+1);
            // -1 represents row hasn't been processed yet
            output.set(0, -1);
            // loop values in input tuple and assign the same to the output
            for (int i=0; i<input.size(); i++) {
            	output.set(i+1, input.get(i));
            }

            if ( input.get(0) instanceof String ) {
				//tuple is been validated
	            output.set(0, 0);
			} else {
				//tuple is invalid
				msg += "1" + "\n";
	            output.set(0, 1);
			}
			if ( input.get(1) instanceof String ) { output.set(0, 0); } else { msg+="2" + "\n"; output.set(0, 1); }
			if ( input.get(2) instanceof String ) { output.set(0, 0); } else { msg+="3" + "\n"; output.set(0, 1); }
			if ( input.get(3) instanceof String ) { output.set(0, 0); } else { msg+="4" + "\n"; output.set(0, 1); }
			if ( input.get(4) instanceof String ) { output.set(0, 0); } else { msg+="5" + "\n"; output.set(0, 1); }
			if ( input.get(5) instanceof String ) { output.set(0, 0); } else { msg+="6" + "\n"; output.set(0, 1); }
			if ( input.get(6) instanceof String ) { output.set(0, 0); } else { msg+="7" + "\n"; output.set(0, 1); }
			if ( input.get(7) instanceof String ) { output.set(0, 0); } else { msg+="8" + "\n"; output.set(0, 1); }
			if ( input.get(8) instanceof String ) { output.set(0, 0); } else { msg+="9" + "\n"; output.set(0, 1); }
			if ( input.get(9) instanceof String ) { output.set(0, 0); } else { msg+="10" + "\n"; output.set(0, 1); }
			if ( input.get(10) instanceof String ) { output.set(0, 0); } else { msg+="11" + "\n"; output.set(0, 1); }
			if ( input.get(11) instanceof Boolean ) { output.set(0, 0); } else { msg+="12" + "\n"; output.set(0, 1); }
			if ( input.get(12) instanceof String ) { output.set(0, 0); } else { msg+="13" + "\n"; output.set(0, 1); }
			if ( input.get(13) instanceof String ) { output.set(0, 0); } else { msg+="14" + "\n"; output.set(0, 1); }
			if ( input.get(14) instanceof String ) { output.set(0, 0); } else { msg+="15" + "\n"; output.set(0, 1); }
			if ( input.get(15) instanceof String ) { output.set(0, 0); } else { msg+="16" + "\n"; output.set(0, 1); }
			if ( input.get(16) instanceof String ) { output.set(0, 0); } else { msg+="17" + "\n"; output.set(0, 1); }
			if ( input.get(17) instanceof Boolean ) { output.set(0, 0); } else { msg+="18" + "\n"; output.set(0, 1); }
			if ( input.get(18) instanceof String ) { output.set(0, 0); } else { msg+="19" + "\n"; output.set(0, 1); }
			if ( input.get(19) instanceof String ) { output.set(0, 0); } else { msg+="20" + "\n"; output.set(0, 1); }
			if ( input.get(20) instanceof String ) { output.set(0, 0); } else { msg+="21" + "\n"; output.set(0, 1); }
			if ( input.get(21) instanceof String ) { output.set(0, 0); } else { msg+="22" + "\n"; output.set(0, 1); }
			if ( input.get(22) instanceof String ) { output.set(0, 0); } else { msg+="23" + "\n"; output.set(0, 1); }
			if ( input.get(23) instanceof String ) { output.set(0, 0); } else { msg+="24" + "\n"; output.set(0, 1); }
			if ( input.get(24) instanceof DateTime ) { output.set(0, 0); } else { msg+="25" + "\n"; output.set(0, 1); }
			if ( input.get(25) instanceof DateTime ) { output.set(0, 0); } else { msg+="26" + "\n"; output.set(0, 1); }
			if ( input.get(26) instanceof Boolean ) { output.set(0, 0); } else { msg+="27" + "\n"; output.set(0, 1); }
			if ( input.get(27) instanceof Double ) { output.set(0, 0); } else { msg+="28" + "\n"; output.set(0, 1); }
			if ( input.get(28) instanceof Double ) { output.set(0, 0); } else { msg+="29" + "\n"; output.set(0, 1); }
			if ( input.get(29) instanceof String ) { output.set(0, 0); } else { msg+="30" + "\n"; output.set(0, 1); }
			if ( input.get(30) instanceof String ) { output.set(0, 0); } else { msg+="31" + "\n"; output.set(0, 1); }
			if ( input.get(31) instanceof Integer ) { output.set(0, 0); } else { msg+="32" + "\n"; output.set(0, 1); }
			if ( input.get(32) instanceof Boolean ) { output.set(0, 0); } else { msg+="33" + "\n"; output.set(0, 1); }
			if ( input.get(33) instanceof String ) { output.set(0, 0); } else { msg+="34" + "\n"; output.set(0, 1); }
			if ( input.get(34) instanceof String ) { output.set(0, 0); } else { msg+="35" + "\n"; output.set(0, 1); }
			if ( input.get(35) instanceof String ) { output.set(0, 0); } else { msg+="36" + "\n"; output.set(0, 1); }
			if ( input.get(36) instanceof String ) { output.set(0, 0); } else { msg+="37" + "\n"; output.set(0, 1); }
			if ( input.get(37) instanceof Boolean ) { output.set(0, 0); } else { msg+="38" + "\n"; output.set(0, 1); }
			if ( input.get(38) instanceof String ) { output.set(0, 0); } else { msg+="39" + "\n"; output.set(0, 1); }
			if ( input.get(39) instanceof Boolean ) { output.set(0, 0); } else { msg+="40" + "\n"; output.set(0, 1); }
			if ( input.get(40) instanceof String ) { output.set(0, 0); } else { msg+="41" + "\n"; output.set(0, 1); }
			if ( input.get(41) instanceof DateTime ) { output.set(0, 0); } else { msg+="42" + "\n"; output.set(0, 1); }
            System.err.println(msg);
			return output;
            
		} catch (ExecException ee) {
            // Throwing an exception will cause the task to fail.
            throw new ExecException("Something bad happened!", ee);
        }
		
	}
	
	public Schema outputSchema(Schema input) {
	    // Check that we were passed two fields
	    if (input.size() != 42) {
	        throw new RuntimeException(
	            "Expected input to have 42 field");
	    }

	    try {
	        // Get the types for both columns and check them. If they are
	        // wrong, figure out what types were passed and give a good error
	        // message.
	        if (input.getField(0).type != DataType.CHARARRAY || 
	        		input.getField(1).type != DataType.CHARARRAY || 
	        		input.getField(2).type != DataType.CHARARRAY || 
	        		input.getField(3).type != DataType.CHARARRAY || 
	        		input.getField(4).type != DataType.CHARARRAY || 
	        		input.getField(5).type != DataType.CHARARRAY || 
	        		input.getField(6).type != DataType.CHARARRAY || 
	        		input.getField(7).type != DataType.CHARARRAY || 
	        		input.getField(8).type != DataType.CHARARRAY || 
	        		input.getField(9).type != DataType.CHARARRAY || 
	        		input.getField(10).type != DataType.CHARARRAY || 
	        		input.getField(11).type != DataType.BOOLEAN || 
	        		input.getField(12).type != DataType.CHARARRAY || 
	        		input.getField(13).type != DataType.CHARARRAY || 
	        		input.getField(14).type != DataType.CHARARRAY || 
	        		input.getField(15).type != DataType.CHARARRAY || 
	        		input.getField(16).type != DataType.CHARARRAY || 
	        		input.getField(17).type != DataType.BOOLEAN || 
	        		input.getField(18).type != DataType.CHARARRAY || 
	        		input.getField(19).type != DataType.CHARARRAY || 
	        		input.getField(20).type != DataType.CHARARRAY || 
	        		input.getField(21).type != DataType.CHARARRAY || 
	        		input.getField(22).type != DataType.CHARARRAY || 
	        		input.getField(23).type != DataType.CHARARRAY || 
	        		input.getField(24).type != DataType.DATETIME || 
	        		input.getField(25).type != DataType.DATETIME || 
	        		input.getField(26).type != DataType.BOOLEAN || 
	        		input.getField(27).type != DataType.DOUBLE || 
	        		input.getField(28).type != DataType.DOUBLE || 
	        		input.getField(29).type != DataType.CHARARRAY || 
	        		input.getField(30).type != DataType.CHARARRAY || 
	        		input.getField(31).type != DataType.INTEGER || 
	        		input.getField(32).type != DataType.BOOLEAN || 
	        		input.getField(33).type != DataType.CHARARRAY || 
	        		input.getField(34).type != DataType.CHARARRAY || 
	        		input.getField(35).type != DataType.CHARARRAY || 
	        		input.getField(36).type != DataType.CHARARRAY || 
	        		input.getField(37).type != DataType.BOOLEAN || 
	        		input.getField(38).type != DataType.CHARARRAY ||
	        		input.getField(39).type != DataType.BOOLEAN || 
	        		input.getField(40).type != DataType.CHARARRAY || 
	        		input.getField(41).type != DataType.DATETIME
	        		) {
	        	
	        	
	            String msg = "Expected input (chararray, chararray, chararray, "
	            		+ "chararray, chararray, chararray, chararray, chararray, "
	            		+ "chararray, chararray, chararray, boolean, chararray, "
	            		+ "chararray, chararray, chararray, chararray, boolean, "
	            		+ "chararray, chararray, chararray, chararray, chararray, "
	            		+ "chararray, datetime, datetime, boolean, double, double, "
	            		+ "chararray, chararray, int, boolean, chararray, chararray, "
	            		+ "chararray, chararray, boolean, chararray, boolean, chararray, "
	            		+ "datetime ), received schema (";
	            for (int i=0; i<input.size(); i++) {
		            msg += DataType.findTypeName(input.getField(i).type);
		            msg += ", ";
	            }
	            msg += ")";
	            
	            throw new RuntimeException(msg);
	        }
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	    
        try{
            Schema tupleSchema = new Schema();
            tupleSchema.add(new Schema.FieldSchema("code", DataType.INTEGER));
            
            for (int i=0; i<input.size(); i++) {
            	tupleSchema.add(input.getField(i));
            }
            
            return new Schema(new Schema.FieldSchema(getSchemaName(null, input), tupleSchema, DataType.TUPLE));
        }catch (Exception e){
                return null;
        }

	}

}
