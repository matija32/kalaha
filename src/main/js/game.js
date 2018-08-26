const React = require('react');
const ReactDOM = require('react-dom');
const axios = require('axios');
import './game.css';


class NormalPit extends React.Component {
    render() {
        let normalPitStyling = this.props.allowedToSowFrom ? "normal-pit-sowable" : "normal-pit-not-sowable"

        return (
            <button className={normalPitStyling} onClick={() => this.props.onClick()} disabled={!this.props.allowedToSowFrom}>
                {this.props.value}
            </button>
        );

    }
}

class KalahaPit extends React.Component {
    render() {
        return (
            <button className="kalaha-pit">
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

    refreshGameStatus() {
        axios.get(`/api/status`)
            .then(res => {
                this.setState({initialized: true, gameStatus: res.data});
            })
    }

    startNewGame() {
        axios.post('/api/restart')
        .catch(error => alert(error.message))
        .then(() => this.refreshGameStatus());
    }

    handleClickOnNormalPit(player, pitId){
        axios.post('/api/play', {
            player: player,
            pitId: pitId
        })
        .catch(error => alert(error.message))
        .then(() => this.refreshGameStatus())
    }

    componentDidMount() {
        this.refreshGameStatus();
    }

    renderNormalPit(player, pitIndex){
        return <NormalPit
            value={this.state.gameStatus.statusPerPlayer[player].stonesInNormalPits[pitIndex]}
            allowedToSowFrom={this.state.gameStatus.statusPerPlayer[player].allowedToSeedFromNormalPit[pitIndex]}
            onClick={() => this.handleClickOnNormalPit(player, pitIndex)}/>
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
                    <label className="player-name">Player 2</label>
                    <KalahaPit value={this.state.gameStatus.statusPerPlayer.TWO.stonesInKalahaPit} />
                    {this.renderNormalPit('TWO', 5)}
                    {this.renderNormalPit('TWO', 4)}
                    {this.renderNormalPit('TWO', 3)}
                    {this.renderNormalPit('TWO', 2)}
                    {this.renderNormalPit('TWO', 1)}
                    {this.renderNormalPit('TWO', 0)}
                </div>
                <div className="board-row">
                    <label className="player-name">Player 1</label>
                    {this.renderNormalPit('ONE', 0)}
                    {this.renderNormalPit('ONE', 1)}
                    {this.renderNormalPit('ONE', 2)}
                    {this.renderNormalPit('ONE', 3)}
                    {this.renderNormalPit('ONE', 4)}
                    {this.renderNormalPit('ONE', 5)}
                    <KalahaPit value={this.state.gameStatus.statusPerPlayer.ONE.stonesInKalahaPit} />
                </div>
                <button onClick={() => this.startNewGame()}>Start a new game!</button>
            </div>
        )
    }
}

ReactDOM.render(
    <Game />,
    document.getElementById('react')
)