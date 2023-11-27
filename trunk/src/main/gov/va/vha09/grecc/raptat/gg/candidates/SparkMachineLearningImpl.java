/**
 *
 */
package src.main.gov.va.vha09.grecc.raptat.gg.candidates;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import src.main.gov.va.vha09.grecc.raptat.gg.core.UserPreferences;

/**
 * @author Glenn Gobbel
 *
 */
public class SparkMachineLearningImpl
{

	/**
	 *
	 */
	public SparkMachineLearningImpl()
	{
		// TODO Auto-generated constructor stub
	}


	public static void main(String[] args)
	{
		int lvgSetting = UserPreferences.INSTANCE.initializeLVGLocation();

		SwingUtilities.invokeLater( new Runnable()
		{
			@Override
			public void run()
			{
				SparkSession spark = SparkSession.builder().appName( "JavaSparkPi" ).getOrCreate();

				JavaSparkContext jsc = new JavaSparkContext( spark.sparkContext() );

				int slices = ( args.length == 1 ) ? Integer.parseInt( args[0] ) : 2;
				int n = 100000 * slices;
				List<Integer> l = new ArrayList<>( n );
				for (int i = 0; i < n; i++ )
				{
					l.add( i );
				}

				JavaRDD<Integer> dataSet = jsc.parallelize( l, slices );

				int count = dataSet.map( integer -> {
					double x = Math.random() * 2 - 1;
					double y = Math.random() * 2 - 1;
					return ( x * x + y * y <= 1 ) ? 1 : 0;
				} ).reduce( (integer, integer2) -> integer + integer2 );

				System.out.println( "Pi is roughly " + 4.0 * count / n );

				spark.stop();
			}
		} );

	}
}
