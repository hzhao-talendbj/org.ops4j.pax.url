import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.providers.file.FileWagon;
import org.sonatype.maven.repository.connector.wagon.WagonProvider;

class ManualWagonProvider
    implements WagonProvider
{

    public Wagon lookup( String roleHint )
        throws Exception
    {
        if ( "file".equals( roleHint ) )
        {
            return new FileWagon();
        }
        return null;
    }

    public void release( Wagon wagon )
    {

    }

}