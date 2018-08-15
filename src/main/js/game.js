const React = require('react');
const ReactDOM = require('react-dom');
const axios = require('axios');
import './game.css';


class NormalPit extends React.Component {
    render() {
        return (
            <button className="normal-pit">
                {this.props.value}
            </button>
        );
    }
}

class KahalaPit extends React.Component {
    render() {
        return (
            <button className="kahala-pit">
                {this.props.value}
            </button>
        );
    }
}

class Game extends React.Component {

    constructor(props) {
        super(props);
        this.state = {initialized : false, gameStatus : {}};
    }

    componentDidMount() {
        axios.get(`/api/status`)
            .then(res => {
                this.setState({initialized : true, gameStatus : res.data});
            })
    }

    render() {
        if (!this.state.initialized) {
            return (
                <div>
                    <div className="status">No game data available</div>
                </div>
            )
        }

        return (
            <div>
                <div className="status">{this.state.gameStatus.message}</div>
                <div className="board-row">
                    <label className="player-name">Player 1</label>
                    <KahalaPit value={this.state.gameStatus.statusPerPlayer.ONE.stonesInKahalaPit} />
                    <NormalPit value={this.state.gameStatus.statusPerPlayer.ONE.stonesInNormalPits[0]} />
                    <NormalPit value={this.state.gameStatus.statusPerPlayer.ONE.stonesInNormalPits[1]} />
                    <NormalPit value={this.state.gameStatus.statusPerPlayer.ONE.stonesInNormalPits[2]} />
                    <NormalPit value={this.state.gameStatus.statusPerPlayer.ONE.stonesInNormalPits[3]} />
                    <NormalPit value={this.state.gameStatus.statusPerPlayer.ONE.stonesInNormalPits[4]} />
                    <NormalPit value={this.state.gameStatus.statusPerPlayer.ONE.stonesInNormalPits[5]} />
                </div>
                <div className="board-row">
                    <label className="player-name">Player 2</label>
                    <NormalPit value={this.state.gameStatus.statusPerPlayer.TWO.stonesInNormalPits[5]} />
                    <NormalPit value={this.state.gameStatus.statusPerPlayer.TWO.stonesInNormalPits[4]} />
                    <NormalPit value={this.state.gameStatus.statusPerPlayer.TWO.stonesInNormalPits[3]} />
                    <NormalPit value={this.state.gameStatus.statusPerPlayer.TWO.stonesInNormalPits[2]} />
                    <NormalPit value={this.state.gameStatus.statusPerPlayer.TWO.stonesInNormalPits[1]} />
                    <NormalPit value={this.state.gameStatus.statusPerPlayer.TWO.stonesInNormalPits[0]} />
                    <KahalaPit value={this.state.gameStatus.statusPerPlayer.TWO.stonesInKahalaPit} />
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