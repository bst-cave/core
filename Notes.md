# Distribution of Work

Don't create actors per user, but use actors to process messages from different users. This implies the user
to be part of the messages.

When the `Poll` messages are sent to the content provider actors, there should be some intelligent algorithm,
which makes sure that as less content providers as possible poll concurrently.

# Top Level Actors

- ContentProvidersActor: All content providers send messages to the index actor
    - GPlusProvider
    - TwitterProvider
    - ...
- IndexActor: Has a reference to an `ElasticClient` instance. Keeps a `Map(User -> Index)`.
  If an index for a user is not found, the actor creates the index asynchronously and re-sends the index message,
  once the index for that user was created successfully. Upon successful indexing the index actor sends a message to
  the stats actor
- SearchActor
- StatsActor