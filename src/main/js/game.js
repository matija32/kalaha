const React = require('react');
const ReactDOM = require('react-dom');
const axios = require('axios');
import './game.css';


class NormalPit extends React.Component {
    render() {
        return (
            <button className="square">
                {this.props.value}
            </button>
        );
    }
}

class KahalaPit extends React.Component {
    render() {
        return (
            <button className="rectangle">
                {this.props.value}
            </button>
        );
    }
}

class Game extends React.Component {

    constructor(props) {
        super(props);
        this.state = {};
    }

    componentDidMount() {
        axios.get(`/api/status`)
            .then(res => {
                this.setState({ status : res.data.message});
            })
    }

    render() {
        return (
            <div>
                <div className="status">{this.state.status}</div>
                <div className="board-row">
                    <KahalaPit value={-2} />
                    <NormalPit value={1} />
                    <NormalPit value={2} />
                    <NormalPit value={3} />
                    <NormalPit value={4} />
                    <NormalPit value={5} />
                    <NormalPit value={6} />
                </div>
                <div className="board-row">
                    <NormalPit value={1} />
                    <NormalPit value={2} />
                    <NormalPit value={3} />
                    <NormalPit value={4} />
                    <NormalPit value={5} />
                    <NormalPit value={6} />
                    <KahalaPit value={-5} />
                </div>
                <button>Start a new game!</button>
            </div>
        )
    }

}

ReactDOM.render(
    <Game />,
    document.getElementById('react')
)