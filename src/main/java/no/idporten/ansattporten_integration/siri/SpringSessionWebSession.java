public class SpringSessionWebSession implements WebSession {

	enum State {
		NEW, STARTED
	}

	private final S session;

	private AtomicReference<State> state = new AtomicReference<>();

	SpringSessionWebSession(S session, State state) {
		this.session = session;
		this.state.set(state);
	}

	@Override
	public void start() {
		this.state.compareAndSet(State.NEW, State.STARTED);
	}

	@Override
	public boolean isStarted() {
		State value = this.state.get();
		return (State.STARTED.equals(value)
				|| (State.NEW.equals(value) && !this.session.getAttributes().isEmpty()));
	}

	@Override
	public Mono<Void> changeSessionId() {
		return Mono.defer(() -> {
			this.session.changeSessionId();
			return save();
		});
	}

	// ... other methods delegate to the original Session
}