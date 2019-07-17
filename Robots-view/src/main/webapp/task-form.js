class TaskForm extends React.Component {
    constructor() {
        super();
        this.state = {
            showModal: false
        };

        this.handleOpenModal = this.handleOpenModal.bind(this);
        this.handleCloseModal = this.handleCloseModal.bind(this);
    }

    handleOpenModal() {
        this.setState({showModal: true});
    }

    handleCloseModal() {
        this.setState({showModal: false});
    }

    render() {
        // return React.createElement("div", null,
        //     React.createElement("button", {onClick: this.handleOpenModal}, "Trigger Modal"),
        //     React.createElement(ReactModal, {isOpen: this.state.showModal, contentLabel: "Minimal Modal Example"},
        //         React.createElement("button", {onClick: this.handleCloseModal}, "Close Modal")));

        return React.createElement(ReactModal, {isOpen: this.state.showModal, contentLabel: "Minimal Modal Example"},
                "привет");

        // return React.createElement("button", {onClick: this.handleOpenModal}, "Trigger Modal");
    }
}

const props = {};

ReactDOM.render(React.createElement(TaskForm, props), document.getElementById('main'))