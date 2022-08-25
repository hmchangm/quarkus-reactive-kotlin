package tw.idv.brandy.arrow.repo

import com.vladsch.kotlin.jdbc.SessionImpl
import io.agroal.api.AgroalDataSource
import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes
import javax.inject.Inject
import javax.sql.DataSource

@ApplicationScoped
class DbConn (val dataSource: AgroalDataSource) {

    fun onStart(@Observes ev: StartupEvent?) {
        SessionImpl.defaultDataSource = {dataSource}
    }

    fun onStop(@Observes ev: ShutdownEvent?) {

    }

}