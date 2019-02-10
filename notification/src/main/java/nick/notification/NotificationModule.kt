package nick.notification

import dagger.Module
import nick.work.WorkAssistedModule

@Module(includes = [WorkerModule::class, WorkAssistedModule::class])
object NotificationModule