import { connect } from "react-redux";

import {
    createEditGroupAction,
    createGetGroupAction
} from "../../../../api/groups/action-creator.groups.api";

import EditGroupDetails from "./EditGroupDetails.screen";
import {
    gammaLoadingFinished,
    gammaLoadingStart
} from "../../../../app/views/gamma-loading/GammaLoading.view.action-creator";

const mapStateToProps = (state, ownProps) => ({
    group: state.groups != null ? state.groups.details : null,
    groupId: ownProps.match.params.id
});

const mapDispatchToProps = dispatch => ({
    groupsChange: (group, groupId) =>
        dispatch(createEditGroupAction(group, groupId)),
    getGroup: groupId => dispatch(createGetGroupAction(groupId)),
    gammaLoadingFinished: () => dispatch(gammaLoadingFinished()),
    gammaLoadingStart: () => dispatch(gammaLoadingStart())
});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(EditGroupDetails);
