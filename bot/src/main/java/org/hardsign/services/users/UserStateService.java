package org.hardsign.services.users;

import org.hardsign.models.UpdateContext;

public interface UserStateService extends UserStateServiceInternal {
    ContextBoundedUserStateService with(UpdateContext context);
}

