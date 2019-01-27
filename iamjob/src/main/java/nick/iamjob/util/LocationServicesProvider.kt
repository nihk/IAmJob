package nick.iamjob.util

interface LocationServicesProvider {

    fun requestLocation(client: LocationClient)
}