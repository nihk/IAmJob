package nick.search.util

import nick.data.model.EphemeralPosition

sealed class PositionAction(val ephemeralPosition: EphemeralPosition) {
    class SaveOrUnsave(ephemeralPosition: EphemeralPosition) : PositionAction(ephemeralPosition)
    class MoreDetails(ephemeralPosition: EphemeralPosition) : PositionAction(ephemeralPosition)
}