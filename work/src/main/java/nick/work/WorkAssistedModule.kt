package nick.work

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@Module(includes = [AssistedInject_WorkAssistedModule::class])
@AssistedModule
interface WorkAssistedModule